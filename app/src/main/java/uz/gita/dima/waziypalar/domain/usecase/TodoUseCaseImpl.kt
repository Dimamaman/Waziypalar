package uz.gita.dima.waziypalar.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.domain.repository.TodoRepository
import javax.inject.Inject

class TodoUseCaseImpl @Inject constructor(
    private val todoRepository: TodoRepository
): TodoUseCase {
    override fun addTodo(todo: Todo): Flow<Result<String>> {
        return todoRepository.addTodo(todo)
    }
}