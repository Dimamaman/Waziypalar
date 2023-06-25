package uz.gita.dima.waziypalar.presenter.viewmodel

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.domain.repository.TodoRepository
import uz.gita.dima.waziypalar.utils.ResultData
import uz.gita.dima.waziypalar.utils.UIHelper.getCurrentTimestamp
import uz.gita.dima.waziypalar.utils.UIHelper.isEmailValid
import uz.gita.dima.waziypalar.BR
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class TaskCreateViewModel @Inject constructor(
    private val taskSource: TodoRepository,
    private val workManager: WorkManager
) : ViewModel(), Observable {

    private val registry = PropertyChangeRegistry()

    private var _emailUnderCheckForAvailability = MutableLiveData<String>()
    val emailUnderCheckForAvailability: LiveData<String> get() = _emailUnderCheckForAvailability

    private var _emailAvailabilityStatus = MutableLiveData<ResultData<String>>(ResultData.DoNothing)
    val emailAvailabilityStatus: LiveData<ResultData<String>> get() = _emailAvailabilityStatus

    private val _taskCreationStatus = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val taskCreationStatus: LiveData<ResultData<*>> get() = _taskCreationStatus
    

    @get: Bindable
    var todo = Todo()
        set(value) {
            if (value != field) field = value
            registry.notifyChange(this, BR.todo)
        }


    @get: Bindable
    val todoAssigneeEmailWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun afterTextChanged(s: Editable) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            s.let { if (it.isEmailValid()) checkEmailAvailability(it.toString()) }
        }
    }


    private fun checkEmailAvailability(email: String) {
       /* viewModelScope.launch {
            val response = taskSource.isUserAvailable(email)
            _emailAvailabilityStatus.postValue(response)
            _emailUnderCheckForAvailability.postValue(email)
            if (response is ResultData.Success) {
                todo.assignee = arrayOf(response.data).toList()
            } else todo.assignee = null
        }*/
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun createTask(item: Todo?) {
        viewModelScope.launch {
            _taskCreationStatus.postValue(ResultData.Loading)
            item?.let {
                it.todoCreationTimeStamp = getCurrentTimestamp()
                when {
                    it.todoBody.isEmpty() ->
                        _taskCreationStatus.postValue(ResultData.Failed("Body can't be empty"))
                    it.todoDate?.isEmpty() == true -> _taskCreationStatus.postValue(
                        ResultData.Failed("Task date can't be empty")
                    )
                    it.taskImage?.isEmpty() == true -> {
                        val response = taskSource.createTask(it)
                        _taskCreationStatus.postValue(response)
                    }
                    else -> {

                    }
                }
            }
        }
    }


    /*private fun createTaskWithImage(item: Todo) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val inputData = workDataOf(
            TASK_BODY to item.todoBody,
            TASK_DESC to item.todoDesc,
            TASK_DATE to item.todoDate,
            TASK_PRIORITY to item.priority,
            TASK_ASSIGNEE to arrayOf(item.assignee?.get(0) as String?),
            TASK_IMAGE_URI to item.taskImage.toString()
        )

        val taskImageUploadWorker =
            OneTimeWorkRequestBuilder<TaskImageUploadWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(constraints)
                .setInputData(inputData)
                .addTag(IMAGE_UPLOAD_WORKER_TAG)
                .build()

        val taskCreationWorker =
            OneTimeWorkRequestBuilder<TaskCreationWorker>()
                .setConstraints(constraints)
                .addTag(TASK_CREATION_WORKER_TAG)
                .build()

        workManager.beginWith(taskImageUploadWorker)
            .then(taskCreationWorker)
            .enqueue()

    }*/


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }
}