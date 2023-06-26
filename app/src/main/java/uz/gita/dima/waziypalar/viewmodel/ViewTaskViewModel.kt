package uz.gita.dima.waziypalar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.data.repository.TodoRepository
import uz.gita.dima.waziypalar.model.Todo
import uz.gita.dima.waziypalar.model.User
import uz.gita.dima.waziypalar.utils.ResultData
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class ViewTaskViewModel @Inject constructor(private val taskSource: TodoRepository) : ViewModel() {

    private var _viewState = MutableLiveData<ResultData<*>>(ResultData.Loading)
    val viewState: LiveData<ResultData<*>> get() = _viewState

    private var _taskDetails = MutableLiveData<Todo>()
    val taskDetails: LiveData<Todo> get() = _taskDetails

    private var _creatorDetails = MutableLiveData<String?>()
    val creatorDetails: LiveData<String?> get() = _creatorDetails


    fun getTaskByTaskId(taskId: String?) {
        viewModelScope.launch {
            val response: Todo? = taskId?.let { taskSource.fetchTaskByTaskId(it) }
            response?.let {
                _taskDetails.postValue(it)
                fetchTaskCreator(it.creator)
            } ?: _viewState.postValue(ResultData.Failed("Check your network!"))
        }
    }


    private fun fetchTaskCreator(creator: String?) {
        viewModelScope.launch {
            val response: User? = creator?.let { taskSource.fetchTaskCreatorDetails(it) }
            response?.email?.let { _creatorDetails.postValue("Assigned by: $it") }
                ?: _creatorDetails.postValue("Assigned by: Anonymous")
            _viewState.postValue(ResultData.Success(null))
        }

    }
}