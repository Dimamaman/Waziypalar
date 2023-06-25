package uz.gita.dima.waziypalar.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.dima.waziypalar.data.model.Todo

interface TodoRepository {

    fun addTodo(todo: Todo): Flow<Result<String>>
}