package uz.gita.dima.waziypalar.presenter.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.BR
import uz.gita.dima.waziypalar.utils.ResultData
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.domain.repository.TodoRepository
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
@ExperimentalCoroutinesApi
@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskSource: TodoRepository
) : ViewModel(), Observable {

    private val registry = PropertyChangeRegistry()

    private var _viewState = MutableLiveData<ResultData<*>>(ResultData.Loading)
    val viewState: LiveData<ResultData<*>> get() = _viewState

    private var _imageUploadState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val imageUploadState: LiveData<ResultData<*>> get() = _imageUploadState

    private var _deleteTaskState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val deleteTaskState: LiveData<ResultData<*>> get() = _deleteTaskState

    private var _updateTaskState = MutableLiveData<ResultData<*>>(ResultData.DoNothing)
    val updateTaskState: LiveData<ResultData<*>> get() = _updateTaskState

    @get: Bindable
    var todo = Todo()
        set(value) {
            if (value != field) field = value
            registry.notifyChange(this, BR.todo)
        }


    fun getTaskByTaskId(taskId: String?) {
        viewModelScope.launch {
            val response = taskId?.let { taskSource.fetchTaskByTaskId(it) }
            if (response != null) {
                todo = response
                _viewState.postValue(ResultData.Success(response))
            } else _viewState.postValue(ResultData.Failed("Check your network!"))
        }
    }


    fun updateTask(item: Todo?) {
        item?.let { taskItem ->
            _updateTaskState.postValue(ResultData.Loading)
            when {
                taskItem.todoBody.isEmpty() ->
                    _updateTaskState.postValue(ResultData.Failed("Body can't be empty"))
                taskItem.todoDate.isNullOrEmpty() ->
                    _updateTaskState.postValue(ResultData.Failed("Date & Time can't be empty"))
                else -> {
                    val taskMap: Map<String, Any?> = mapOf(
                        "todoBody" to taskItem.todoBody,
                        "todoDesc" to taskItem.todoDesc,
                        "todoDate" to taskItem.todoDate,
                        "priority" to taskItem.priority
                    )
                    viewModelScope.launch {
                        val response = taskSource.updateTask(taskItem.docId, taskMap)
                        response.let { _updateTaskState.postValue(ResultData.Success(it)) }
                    }
                }
            }
        }
    }


    fun changeTaskStatus() = viewModelScope.launch {
        val map =
            if (todo.isCompleted) {
                Log.d("TTTT","Completed -> ${todo.isCompleted}")
                mapOf("isCompleted" to true)
            }
            else {
                Log.d("TTTT","Completed -> ElSE de......")
                mapOf("isCompleted" to false)
            }
        todo.docId.let {
            Log.d("TTT","DOCid -> $it")
            taskSource.markTaskComplete(map, it)
        }
    }


    fun deleteTask() = viewModelScope.launch {
        _deleteTaskState.postValue(ResultData.Loading)
        val response = todo.docId.let {
            taskSource.deleteTask(it)
        }
        if (response is ResultData.Success && response.data == true)
            _deleteTaskState.postValue(ResultData.Success(true))
        else _deleteTaskState.postValue(ResultData.Failed("Unable to delete. Please retry."))
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.add(callback)
    }


    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        registry.remove(callback)
    }
}