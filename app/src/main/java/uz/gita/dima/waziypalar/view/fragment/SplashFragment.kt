package uz.gita.dima.waziypalar.view.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.utils.Constants.IS_FIRST_TIME
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkDestination()
    }

    private fun checkDestination() {
        val timer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (firebaseAuth.currentUser == null || sharedPreferences.getBoolean(
                        IS_FIRST_TIME,
                        true
                    )
                ) {
                    goToAuthFragment()
                }
                else if (firebaseAuth.currentUser != null) goToTaskFragment()
                else goToAuthFragment()
            }
        }
        timer.start()
    }

    private fun goToTaskFragment() {
        findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToTaskFragment())
    }

    private fun goToAuthFragment() {
        lifecycleScope.launchWhenResumed {
            findNavController().navigate(R.id.action_splashFragment_to_authFragment)
        }
    }
}