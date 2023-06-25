package uz.gita.dima.waziypalar.presenter.screens.addedit

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.databinding.ScreenAddEditBinding
import uz.gita.dima.waziypalar.presenter.adapter.setPriorityText
import uz.gita.dima.waziypalar.util.DateTimePicker
import uz.gita.dima.waziypalar.util.UIHelper
import uz.gita.dima.waziypalar.util.beautifyDateTime
import uz.gita.dima.waziypalar.util.toLocalDateTime
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class AddEditTodo : Fragment(R.layout.screen_add_edit) {

    @Inject
    lateinit var dateTimePicker: DateTimePicker

    private var binding: ScreenAddEditBinding? = null
    private val viewModel: AddEditViewModel by viewModels<AddEditViewModelImpl>()


    private lateinit var todo: Todo
    private lateinit var date: String

    private var isClickable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ScreenAddEditBinding.inflate(inflater, container, false)
        return binding?.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.message.observe(viewLifecycleOwner, message)

        binding?.tvTaskInput?.requestFocus()
        handleBackStackEntry()
        setListeners()
        setObservers()

        binding?.apply {
            btnCreateTask.setOnClickListener {
                if (tvTaskInput.text.isNotEmpty() && tvTaskDescInput.text.isNotEmpty() && isClickable) {
                    val id = (0..10000000).random()
                    val title = tvTaskInput.text.toString()
                    val description = tvTaskDescInput.text.toString()
                    val date = date
                    val priority = addPriority.text.toString()

                    todo = Todo(id, title, description, date, priority)

                    viewModel.addTodo(todo)
                } else {
                    binding?.root?.let { it1 -> UIHelper.showSnack(it1, "Fields is empty") }
                }
            }
        }
    }

    private fun setListeners() {
        binding?.apply {
            imgBack.setOnClickListener {
                //  viewmodelden navigate qilamiz
                findNavController().navigateUp()
            }
            tvSelectDate.setOnClickListener {
                dateTimePicker.openDateTimePicker(requireContext())
            }

            addPriority.setOnClickListener {
                // bunida viewmodelge shigariw kk
                findNavController()
                    .navigate(R.id.action_addEditTodo_to_prioritySelectionDialog)
            }
        }
    }

    private fun handleBackStackEntry() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.addEditTodo)
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && navBackStackEntry.savedStateHandle.contains("PRIORITY")) {
                val result = navBackStackEntry.savedStateHandle.get<Int>("PRIORITY")
                result?.let {

                    binding?.addPriority?.setPriorityText(it)
//                    isClickable = true
//                    viewModel.todo.priority = it
                }
            }
        }
        navBackStackEntry.getLifecycle().addObserver(observer)

        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.getLifecycle().removeObserver(observer)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers() {
        dateTimePicker.epochFormat.observe(viewLifecycleOwner) { epochLong ->
//            viewModel.todo.todoDate = epochLong.toString()
            date = epochLong.toString()
            binding?.tvSelectDate?.text = epochLong.toString().toLocalDateTime()?.beautifyDateTime()
            isClickable = true
        }

/*        viewModel.taskCreationStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                is ResultData.Loading -> showToast(requireContext(), "Creating")
                is ResultData.Failed -> showToast(requireContext(), status.message.toString())
                is ResultData.Success -> {
                    val taskItem = (status.data as Todo).apply { this.docId = generateDocId() }
                    setTaskAlarm(taskItem)
                    showToast(requireContext(), "Task Created")
                    findNavController().navigateUp()
                }
                else -> {
                }
            }
        }*/

/*        workManager.getWorkInfosByTagLiveData(IMAGE_UPLOAD_WORKER_TAG).observe(
            viewLifecycleOwner
        ) { workInfoList ->
            if (workInfoList.size != 0 && workInfoList != null) {
                val workInfo = workInfoList[0]
                when (workInfo.state) {
                    WorkInfo.State.SUCCEEDED -> showSnack(requireView(), "Image Uploaded")
                    WorkInfo.State.FAILED -> showSnack(requireView(), "Upload failed!")
                    else -> {
                    }
                }
            }
        }*/

        /*      workManager.getWorkInfosByTagLiveData(TASK_CREATION_WORKER_TAG).observe(
                  viewLifecycleOwner
              ) { workInfoList ->
                  if (workInfoList.size != 0 && workInfoList != null) {
                      val workInfo = workInfoList[0]
                      when (workInfo.state) {
                          WorkInfo.State.SUCCEEDED -> {
                              showSnack(requireView(), "Task Created")
                              workManager.pruneWork()
                              findNavController().navigateUp()
                          }
                          WorkInfo.State.FAILED -> showSnack(requireView(), "Task creation failed!")
                          else -> {
                          }
                      }
                  }
              }*/
    }


    private val message = Observer<String> {
        UIHelper.showSnack(binding!!.imgBack, it)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}