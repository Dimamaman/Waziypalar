package uz.gita.dima.waziypalar.view.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.appsflyer.AppsFlyerLib
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.viewmodel.AuthViewModel
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.databinding.ActivityMainBinding
import uz.gita.dima.waziypalar.receiver.AlarmReceiver
import uz.gita.dima.waziypalar.utils.AuthManager
import uz.gita.dima.waziypalar.utils.CacheManager
import uz.gita.dima.waziypalar.utils.Constants.ACTION_SHOW_TASK_FRAGMENT
import uz.gita.dima.waziypalar.utils.Constants.ANDROID_OREO
import uz.gita.dima.waziypalar.utils.Constants.DEVICE_ANDROID_VERSION
import uz.gita.dima.waziypalar.utils.UIHelper.showSnack
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var appsFlyerLib: AppsFlyerLib
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var cache: CacheManager
    private lateinit var authManager: AuthManager
    private val viewModel: AuthViewModel by viewModels()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appsFlyerLib.start(this)

        authManager = AuthManager(this)

        managePendingAlarm()

        navigateToGlobalFragment(intent)
        setScreenUI()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            navigateToGlobalFragment(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cache.clearCache(this)
    }


    // TODO: Find work around
    @SuppressLint("ResourceAsColor", "SourceLockedOrientationActivity")
    private fun setScreenUI() {
        // To support portrait view in API 26
        if (DEVICE_ANDROID_VERSION != ANDROID_OREO) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }


    // Enables the AlarmReceiver
    private fun managePendingAlarm() {
        val receiver = ComponentName(applicationContext, AlarmReceiver::class.java)

        applicationContext.packageManager?.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }


    private fun navigateToGlobalFragment(intent: Intent) {
        if (intent.action == ACTION_SHOW_TASK_FRAGMENT)
            findNavController(R.id.navHostFragment).navigate(R.id.action_global_taskFragment)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            authManager.handleAuth(requestCode, resultCode, data) { isSuccessful, error ->
                if (isSuccessful) {
                    val userDetails = authManager.userDetails
                    userDetails?.let {
                        viewModel.saveUserData(it)
                    }
                    findNavController(R.id.navHostFragment).navigate(R.id.action_global_taskFragment)
                } else showSnack(
                    findViewById(R.id.activityMain), getString(
                        R.string.prompt_failed_to_login,
                        error?.localizedMessage ?: "Unknown Error"
                    )
                )
            }
        }
    }
}

