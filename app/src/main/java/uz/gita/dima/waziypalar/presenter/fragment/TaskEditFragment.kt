package uz.gita.dima.waziypalar.presenter.fragment

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.databinding.FragmentTaskEditBinding
import uz.gita.dima.waziypalar.presenter.adapter.setNotificationDateTime
import uz.gita.dima.waziypalar.presenter.adapter.setPriorityText
import uz.gita.dima.waziypalar.presenter.viewmodel.EditTaskViewModel
import uz.gita.dima.waziypalar.utils.*
import uz.gita.dima.waziypalar.utils.UIHelper.hideKeyboard
import uz.gita.dima.waziypalar.utils.UIHelper.showSnack
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class TaskEditFragment : Fragment(R.layout.fragment_task_edit) {

    private val binding by viewLifecycleLazy { FragmentTaskEditBinding.bind(requireView()) }

    private val viewModel: EditTaskViewModel by viewModels()

    private val taskIdFromArgs by lazy {
        TaskEditFragmentArgs.fromBundle(requireArguments()).taskId
    }

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var alarmManager: AlarmManager

    @Inject
    lateinit var pendingIntent: PendingIntent

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    private var pickedImage: Uri? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpScreen()
        setListener()
        handleBackStackEntry()
        setObservers()
    }

    private fun handleBackStackEntry() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.taskEditFragment)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("PRIORITY")) {
                val result = navBackStackEntry.savedStateHandle.get<Int>("PRIORITY")
                result?.let {
                    binding.tvTaskPriority.setPriorityText(it)
                    viewModel.todo.priority = it
                }
            }
        }
        navBackStackEntry.lifecycle.addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(observer)
            }
        })
    }


    private fun setUpScreen() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewmodel = viewModel
        }
        taskIdFromArgs?.let { fetchTask(it) } ?: showSnack(requireView(), "Can't find task Id")
    }


    private fun setListener() {
        binding.apply {
//            imgUploadTaskImg.setOnClickListener {
//                if (isNetworkAvailable()) {
////                    selectImage()
//                }
//                else showSnack(requireView(), "Check Internet!")
//            }
            tvTaskPriority.setOnClickListener {
                findNavController().navigate(R.id.action_taskEditFragment_to_prioritySelectionDialog)
            }
            tvDeleteTask.setOnClickListener { deleteTask(taskIdFromArgs) }
            tvSelectDate.setOnClickListener { dateTimePicker.openDateTimePicker(requireContext()) }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers() {
        dateTimePicker.epochFormat.observe(viewLifecycleOwner) { epoch ->
            epoch?.let { dateTime ->
                binding.tvSelectDate.setNotificationDateTime(dateTime.toString())
                viewModel.todo.todoDate = dateTime.toString()
            }
        }
        viewModel.updateTaskState.observe(viewLifecycleOwner) { state ->
            state?.let { if (it is ResultData.Loading) hideKeyboard() }
        }
        viewModel.viewState.observe(viewLifecycleOwner) { state ->
            state?.let { if (it is ResultData.Success) binding.task = it.data as Todo }
        }
    }


    private fun fetchTask(taskId: String) {
        if (isNetworkAvailable()) viewModel.getTaskByTaskId(taskId)
        else showSnack(requireView(), "Check internet connection.")
    }


    /*private fun changeImage(pickedImage: Uri?) {
        if (isNetworkAvailable()) {
            lifecycleScope.launch {
                viewModel.uploadImage(pickedImage, taskIdFromArgs!!)
                viewModel.imageUploadState.observe(viewLifecycleOwner) { response ->
                    response?.let {
                        if (it is ResultData.Success) {
                            logMessage("${it.data}")
                            viewModel.todo.taskImage = "${it.data}"
                        }
                    }
                }
            }
        } else showSnack(requireView(), "Check internet connection.")
    }*/


    private fun deleteTask(docId: String?) {
        if (isNetworkAvailable()) {
            dialog.setPositiveButton("Yes") { dialogInterface, _ ->
                viewModel.deleteTask()
                viewModel.deleteTaskState.observe(viewLifecycleOwner) { response ->
                    if (response is ResultData.Success<*>) {
                        requireContext().cancelAlarmedNotification(docId!!)
                        findNavController().navigate(R.id.action_taskEditFragment_to_taskFragment)
                        dialogInterface.dismiss()
                        showSnack(requireView(), "Task deleted")
                    }
                }
            }.create().show()
        } else showSnack(requireView(), "Check internet connection.")
    }
}