package uz.gita.dima.waziypalar.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.dima.waziypalar.data.model.Todo
import uz.gita.dima.waziypalar.utils.ResultData

interface TodoRepository {

    // For FireStore
    suspend fun createTask(taskItem: Todo): ResultData<Todo>
    suspend fun createTaskUsingWorker(taskMap: HashMap<*, *>): String
    suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchTaskByTaskId(taskId: String): Todo?
    suspend fun updateTask(taskId: String, map: Map<String, Any?>): ResultData<Boolean>
    suspend fun deleteTask(docId: String, taskImageLink: String?): ResultData<Boolean>
    suspend fun markTaskComplete(map: Map<String, Any?>, docId: String): Boolean
}