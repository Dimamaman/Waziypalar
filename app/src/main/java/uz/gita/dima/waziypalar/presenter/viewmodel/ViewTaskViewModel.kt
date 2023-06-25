package uz.gita.dima.waziypalar.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.domain.repository.TodoRepository
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
            } ?: _viewState.postValue(ResultData.Failed("Check your network!"))
        }
    }
}