package uz.gita.dima.waziypalar.workmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.data.todorepository.TodoRepositoryImp
import uz.gita.dima.waziypalar.presenter.MainActivity
import uz.gita.dima.waziypalar.utils.Notify
import uz.gita.dima.waziypalar.utils.UIHelper.getCurrentTimestamp
import uz.gita.dima.waziypalar.utils.startAlarmedNotification
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltWorker
class TaskCreationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val alarmManager: AlarmManager
) :
    CoroutineWorker(context, workerParameters) {

    @Inject
    lateinit var notify: Notify
    private val context = applicationContext
    private val repository = TodoRepositoryImp()
//    private val currentUser = repository.userDetails

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        val taskCreator = checkNotNull(currentUser?.uid)
        val taskBody = checkNotNull(inputData.getString(TASK_BODY))
        val taskDesc = checkNotNull(inputData.getString(TASK_DESC))
        val taskDate = checkNotNull(inputData.getString(TASK_DATE))
        val taskAssigneeList = (inputData.getStringArray(TASK_ASSIGNEE))?.toList()
//        val imageUri = inputData.getString(UPLOADED_IMAGE_URI)

        val taskMap = hashMapOf(
            "todoBody" to taskBody,
            "todoDesc" to taskDesc,
            "todoDate" to taskDate,
            "todoCreationTimeStamp" to getCurrentTimestamp(),
            "isCompleted" to false,
            "assignee" to taskAssigneeList,
            "priority" to 1,
        )

        val intent = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val response: String = repository.createTaskUsingWorker(taskMap)
        if (response.isNotEmpty()) {
            context.startAlarmedNotification(
                id = response,
                taskBody,
                taskDesc,
                taskDate.toLong(),
                alarmManager
            )
            showNotification(taskBody, context.getString(R.string.task_added_success), intent)
            Result.success()
        } else {
            showNotification(taskBody, context.getString(R.string.task_added_failure), intent)
            Result.failure()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(title: String, caption: String, intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        notify.showNotification(context) {
            notificationId = title.hashCode()
            notificationTitle = title
            notificationBody = caption
            this.pendingIntent = pendingIntent
        }
    }

    companion object {
        const val TASK_BODY = "TASK_BODY"
        const val TASK_DESC = "TASK_DESC"
        const val TASK_DATE = "TASK_DATE"
        const val TASK_PRIORITY = "TASK_PRIORITY"
        const val TASK_ASSIGNEE = "TASK_ASSIGNEE"
        const val TASK_IMAGE_URI = "TASK_IMAGE_URI"
        const val TASK_CREATION_WORKER_TAG = "task_creation_worker"
    }
}