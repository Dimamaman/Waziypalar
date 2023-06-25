package uz.gita.dima.waziypalar.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.dima.waziypalar.data.model.Todo

interface TodoUseCase {
    fun addTodo(todo: Todo): Flow<Result<String>>
}