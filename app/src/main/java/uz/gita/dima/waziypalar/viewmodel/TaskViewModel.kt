package uz.gita.dima.waziypalar.viewmodel

import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.data.repository.QuoteRepository
import uz.gita.dima.waziypalar.data.repository.TodoRepository
import uz.gita.dima.waziypalar.model.Todo
import uz.gita.dima.waziypalar.utils.Constants.IS_AFTER
import uz.gita.dima.waziypalar.utils.Constants.IS_BEFORE
import uz.gita.dima.waziypalar.utils.Constants.QUOTE
import uz.gita.dima.waziypalar.utils.Constants.QUOTE_AUTHOR
import uz.gita.dima.waziypalar.utils.UIHelper.greetingMessage
import uz.gita.dima.waziypalar.utils.compareWithToday
import javax.inject.Inject


@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskSource: TodoRepository,
    private val quoteSource: QuoteRepository,
    private val preferences: SharedPreferences
) : ViewModel() {

    private var _firstName = MutableLiveData<String>()
    val firstName get() = _firstName

    private var _loadingState = MutableLiveData<Boolean>()
    val loadingState get() = _loadingState

    private var _taskListSize = MutableLiveData<Int>()
    val taskListSize get() = _taskListSize

    private var _assignedTaskList = MutableLiveData<List<Todo>>()
    val assignedTaskList get() = _assignedTaskList

    private var _completedTaskList = MutableLiveData<List<Todo>>()
    val completedTaskList get() = _completedTaskList

    private var _upcomingTaskList = MutableLiveData<List<Todo>>()
    val upcomingTaskList get() = _upcomingTaskList

    private var _overdueTaskList = MutableLiveData<List<Todo>>()
    val overdueTaskList get() = _overdueTaskList

    private var _quoteText = MutableLiveData<String?>()
    val quote get() = _quoteText

    private var _quoteAuthor = MutableLiveData<String?>()
    val author get() = _quoteAuthor

    init {
        setLoadingState(true)
        getUserFirstName()
        getAllUnassignedTask()
        getAllAssignedTasks()
    }

    private fun getUserFirstName() {
        val firstName = taskSource.userDetails?.displayName
            ?.split(" ")
            ?.toMutableList()
            ?.firstOrNull()
        _firstName.value = "${greetingMessage()}, $firstName"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAllUnassignedTask() {
        viewModelScope.launch {
            try {
                taskSource.fetchAllUnassignedTask().collect { allTodoList ->
                    Log.d("TTT","ASSigned -> $allTodoList")
                    if (allTodoList.size != 0) {
                        _taskListSize.value = allTodoList.size
                        _completedTaskList.value = getCompletedTaskList(allTodoList)
                        _upcomingTaskList.value = getUpcomingTaskList(allTodoList)
                        _overdueTaskList.value = getOverDueTaskList(allTodoList)
                    }
                    else return@collect
                }
            } catch (e: Exception) {
                setLoadingState(false)
            }
        }
    }

    private fun getAllAssignedTasks() {
        viewModelScope.launch {
            try {
                taskSource.fetchOnlyAssignedTask().collect { assignedTask ->
                    assignedTaskList.value = assignedTask
                    Log.d("TTT","ASSigned -> $assignedTask")
                    if (assignedTask.size != 0) {
                        getAssignedTaskList(assignedTask)
                        setLoadingState(false)
                    } else {
                        getQuote()
                    }
                }
            } catch (e: Exception) {
                setLoadingState(false)
            }
        }
    }

    private fun getQuote() {
        val quote = preferences.getString(QUOTE, "")
        if (quote?.isNotBlank() == true) {
            _quoteText.value = quote
            _quoteAuthor.value = preferences.getString(QUOTE_AUTHOR, "")
            _loadingState.value = false
        } else {
            viewModelScope.launch {
                val todayQuote = quoteSource.fetchQuote()
                _quoteText.value = "${todayQuote.text}"
                _quoteAuthor.value = "${todayQuote.author}"
                setLoadingState(false)
            }
        }
    }


    private fun getCompletedTaskList(list: List<Todo>) = list.filter {
        it.isCompleted
    }.sortedWith(compareByDescending(nullsLast()) {
        it.todoDate }
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getUpcomingTaskList(list: List<Todo>) =
        list.filter { it.todoDate?.compareWithToday() == IS_AFTER && !it.isCompleted }
            .sortedWith(compareByDescending(nullsLast()) { it.todoDate })


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getOverDueTaskList(list: List<Todo>) =
        list.filter { it.todoDate?.compareWithToday() == IS_BEFORE && !it.isCompleted }
            .sortedWith(compareByDescending(nullsLast()) { it.todoDate })

    private fun getAssignedTaskList(list: List<Todo>) =
        list.sortedWith(compareByDescending(nullsLast()) { it.todoDate })

    private fun setLoadingState(state: Boolean) {
        _loadingState.value = state
    }
}