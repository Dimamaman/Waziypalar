package uz.gita.dima.waziypalar.view.fragment

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.databinding.FragmentTaskViewBinding
import uz.gita.dima.waziypalar.utils.ResultData
import uz.gita.dima.waziypalar.utils.UIHelper.showSnack
import uz.gita.dima.waziypalar.utils.isNetworkAvailable
import uz.gita.dima.waziypalar.utils.viewLifecycleLazy
import uz.gita.dima.waziypalar.viewmodel.ViewTaskViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TaskViewFragment : Fragment(R.layout.fragment_task_view) {

    private val binding by viewLifecycleLazy { FragmentTaskViewBinding.bind(requireView()) }

    private val viewModel: ViewTaskViewModel by viewModels()

    private val animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.slide_to_top)
    }

    private val taskIdFromArgs by lazy {
        TaskViewFragmentArgs.fromBundle(requireArguments()).taskId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListener()
        setObservers()
    }


    private fun setUpScreen() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        taskIdFromArgs?.let { fetchTask(it) } ?: showSnack(requireView(), "Can't find task Id")
    }


    private fun setListener() {
        with(binding) {
            fragmentTaskViewImgCalendar.setOnClickListener { }
            fragmentTaskViewImgBack.setOnClickListener { findNavController().navigateUp() }
        }
    }


    private fun setObservers() {
        viewModel.viewState.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it is ResultData.Success)
                    binding.fragmentViewBottomLayout.startAnimation(animation)
            }
        }
    }


    private fun fetchTask(taskId: String) {
        if (isNetworkAvailable()) viewModel.getTaskByTaskId(taskId)
        else showSnack(requireView(), "Check internet connection.")
    }

}