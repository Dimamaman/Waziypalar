package uz.gita.dima.waziypalar.presenter.fragment

import android.annotation.*
import android.app.*
import android.content.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.activity.*
import androidx.annotation.RequiresApi
import androidx.databinding.*
import androidx.fragment.app.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import uz.gita.dima.waziypalar.presenter.viewmodel.*
import uz.gita.dima.waziypalar.R
import uz.gita.dima.waziypalar.databinding.*
import uz.gita.dima.waziypalar.presenter.adapter.main_adapter.TodoAdapter
import uz.gita.dima.waziypalar.presenter.adapter.viewpager_adapter.ViewPagerAdapter
import uz.gita.dima.waziypalar.presenter.viewmodel.TaskViewModel
import uz.gita.dima.waziypalar.utils.SliderTransformer
import uz.gita.dima.waziypalar.utils.UIHelper.showSnack
import uz.gita.dima.waziypalar.utils.feedBack
import uz.gita.dima.waziypalar.utils.isNetworkAvailable
import uz.gita.dima.waziypalar.utils.shareApp
import javax.inject.*

@AndroidEntryPoint
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@SuppressLint("SetTextI18n")
class TaskFragment : Fragment(R.layout.fragment_task) {

    private var binding: FragmentTaskBinding? = null

    @Inject
    lateinit var dialog: AlertDialog.Builder

    @Inject
    lateinit var assignedTaskAdapter: ViewPagerAdapter

    @Inject
    lateinit var overdueAdapter: TodoAdapter

    @Inject
    lateinit var upcomingAdapter: TodoAdapter

    @Inject
    lateinit var completedAdapter: TodoAdapter

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: TaskViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_task, container, false)
        return binding?.apply {
            viewmodel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }?.root
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpScreen()
        setListeners()
        setObservers()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finishAffinity()
        }
    }

    private fun setUpScreen() {
        binding?.apply {
            layoutTaskHolder.layoutUpcomingTask.rvUpcomingTaskList.adapter = upcomingAdapter
            layoutTaskHolder.layoutOverdueTask.rvOverdueTaskList.adapter = overdueAdapter
            layoutTaskHolder.layoutCompletedTask.rvCompletedTaskList.adapter = completedAdapter

            layoutTaskHolder.layoutAssignedTask.vpAssignedTaskList.apply {
                adapter = assignedTaskAdapter
                offscreenPageLimit = 5
                setPageTransformer(SliderTransformer(5))
            }
        }
    }

    private fun setListeners() {
        binding?.apply {
            layoutTaskBar.imgMenu.setOnClickListener { showPopupMenu(binding?.layoutTaskBar!!.imgMenu) }
            layoutTaskHolder.layoutNoTask.btnAddTask.setOnClickListener { goToCreateTaskFragment() }
            btnCreateTask.setOnClickListener { goToCreateTaskFragment() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setObservers() {
        viewModel.assignedTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { assignedTaskAdapter.submitList(it) }
        }

        viewModel.overdueTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { overdueAdapter.submitList(it) }
        }

        viewModel.upcomingTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { upcomingAdapter.submitList(it) }
        }

        viewModel.completedTaskList.observe(viewLifecycleOwner) { list ->
            list?.let { completedAdapter.submitList(it) }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.fragment_task_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_share_app -> shareApp()
                R.id.menu_feedback -> feedBack()
            }
            true
        }
    }


    private fun goToCreateTaskFragment() {
        if (isNetworkAvailable())
            findNavController().navigate(R.id.action_taskFragment_to_taskCreateFragment)
        else showSnack(requireView(), "Check Internet!")
    }


    @SuppressLint("CommitPrefEdits")
    /*private fun logOutUser() {
        dialog.setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { dialogInterface, _ ->
                authManager.signOut().observe(viewLifecycleOwner) {
                    if (it is ResultData.Success) {
                        sharedPreferences.edit().putBoolean(IS_FIRST_TIME, true).apply()
                        dialogInterface.dismiss()
                        findNavController().navigate(R.id.action_taskFragment_to_splashFragment)
                    } else if (it is ResultData.Failed) showSnack(requireView(), "Logout failed!")
                }
            }.create().show()
    }*/


    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}