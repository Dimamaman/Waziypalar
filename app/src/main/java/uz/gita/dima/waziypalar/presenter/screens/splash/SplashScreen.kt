package uz.gita.dima.waziypalar.presenter.screens.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.databinding.ScreenSplashBinding
import uz.gita.dima.waziypalar.workmanager.MyWorker

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen: Fragment(R.layout.screen_splash) {
    private val viewModel : SplashViewModel by viewModels<SplashViewModelImpl>()
    private val binding by viewBinding(ScreenSplashBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.navigate()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val myWork = OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext())
            .enqueue(myWork)

        WorkManager.getInstance(requireContext())
            .getWorkInfoByIdLiveData(myWork.id)
            .observe(viewLifecycleOwner) { workInfo ->
                when(workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        viewModel.navigate()
                    }
                    else -> { }
                }
            }

        viewModel.internetConnection.observe(viewLifecycleOwner) {
            if (it) {
                binding.imgNoInternet.visibility = View.INVISIBLE
            } else {
                binding.imgNoInternet.visibility = View.VISIBLE
            }
        }
    }
}