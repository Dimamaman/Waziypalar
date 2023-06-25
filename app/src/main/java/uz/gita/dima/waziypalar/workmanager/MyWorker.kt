package uz.gita.dima.waziypalar.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(content: Context, workerParameters: WorkerParameters) : Worker(content, workerParameters) {

    override fun doWork(): Result {
        return Result.success()
    }
}