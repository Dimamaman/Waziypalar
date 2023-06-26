package uz.gita.dima.waziypalar

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.utils.Constants.QUOTE_WORKER_TAG
import uz.gita.dima.waziypalar.utils.UIHelper.logMessage
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltAndroidApp
class TodoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private val devKey = "aUeLMSf4msnzSfiTVSyZvC"

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()

    override fun onCreate() {
        super.onCreate()

        // set up work manager
        /*delayedInit()*/

        // setup AppsFlyer tracking
        initiateTracking()
    }

    private fun initiateTracking() {
        val conversionDataListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                data?.let { cvData ->
                    cvData.map { logMessage("conversion_attribute:  ${it.key} = ${it.value}") }
                }
            }

            override fun onConversionDataFail(p0: String?) {
                logMessage("error onAttributionFailure: $p0")
            }

            override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                p0?.let { logMessage("onAppOpen_attribute: $it") }
            }

            override fun onAttributionFailure(p0: String?) {
                logMessage("error onAttributionFailure: $p0")
            }
        }
        AppsFlyerLib.getInstance().init(devKey, conversionDataListener, this)
    }
}