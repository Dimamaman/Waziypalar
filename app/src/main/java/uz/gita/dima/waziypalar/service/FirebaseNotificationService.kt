package uz.gita.dima.waziypalar.service

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.utils.Notify
import uz.gita.dima.waziypalar.utils.UIHelper.logMessage
import javax.inject.Inject

/** This class serves the purpose of receiving firebase push notifications with data
 *  and showing them
 *  */

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FirebaseNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notify: Notify

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.let {
            val title = checkNotNull(it.data["title"].toString())
            val body = checkNotNull(it.data["message"].toString())
            val docId = checkNotNull(it.data["document"].toString())

            logMessage(message.data.toString())

            notify.showNotification(applicationContext) {
                notificationId = docId.hashCode()
                notificationTitle = title
                notificationBody = body
            }
        }
    }
}