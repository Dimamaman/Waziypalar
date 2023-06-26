package uz.gita.dima.waziypalar.workers

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import uz.gita.dima.waziypalar.data.repository.QuoteRepository
import uz.gita.dima.waziypalar.utils.Constants.QUOTE
import uz.gita.dima.waziypalar.utils.Constants.QUOTE_AUTHOR
import uz.gita.dima.waziypalar.utils.Constants.QUOTE_WORKER_ID
import uz.gita.dima.waziypalar.utils.Notify


/**
 * This helps to make one network calls each day to get daily qoute and saves in shared preferences.
 */

@HiltWorker
class QuoteFetchWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val preferences: SharedPreferences,
    private val repository: QuoteRepository,
    private val notify: Notify
) :
    CoroutineWorker(context, workerParameters) {

    private val context = applicationContext

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val response = repository.fetchQuote()
        if (response.text?.isNotEmpty() == true) {
            preferences.edit {
                putString(QUOTE, response.text)
                putString(QUOTE_AUTHOR, "${response.author}")
                apply()
            }
            Result.success()
        } else {
            showErrorNotification()
            Result.failure()
        }
        Result.failure()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showErrorNotification() {
        notify.showNotification(context) {
            notificationId = QUOTE_WORKER_ID
            notificationTitle = "Ehh! Syncing Failed"
            notificationBody = "Something stopped the process."
        }
    }
}