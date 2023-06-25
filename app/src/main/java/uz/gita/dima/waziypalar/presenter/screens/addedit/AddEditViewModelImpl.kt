package uz.gita.dima.waziypalar.presenter.screens.addedit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.domain.usecase.TodoUseCase
import javax.inject.Inject

@HiltViewModel
class AddEditViewModelImpl @Inject constructor(
    private val todoUseCase: TodoUseCase,
    private val addEditDirection: AddEditDirection
) : ViewModel(), AddEditViewModel {
    override val message =  MutableLiveData<String>()

    override fun addTodo(todo: Todo) {
        todoUseCase.addTodo(todo).onEach {
            it.onSuccess { success ->
                message.value = success
                addEditDirection.back()
            }

            it.onFailure { error ->
                message.value = error.message
            }
        }.launchIn(viewModelScope)
    }
}