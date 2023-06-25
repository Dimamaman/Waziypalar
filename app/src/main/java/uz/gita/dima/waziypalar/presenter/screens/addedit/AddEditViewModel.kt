package uz.gita.dima.waziypalar.presenter.screens.addedit

import androidx.lifecycle.LiveData
import uz.gita.dima.waziypalar.data.model.Todo

interface AddEditViewModel {
    val message: LiveData<String>
    fun addTodo(todo: Todo)
}