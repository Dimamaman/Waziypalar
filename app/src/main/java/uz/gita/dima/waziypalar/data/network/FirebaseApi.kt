package uz.gita.dima.waziypalar.data.network

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import uz.gita.dima.waziypalar.model.Todo
import uz.gita.dima.waziypalar.model.User
import uz.gita.dima.waziypalar.utils.ResultData

interface FirebaseApi {

    // For storing user data
    suspend fun saveUser(user: FirebaseUser)

    // For Firestore
    suspend fun createTask(taskItem: Todo): ResultData<Todo>
    suspend fun createTaskUsingWorker(taskMap: HashMap<*, *>): String
    suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>>
    suspend fun fetchTaskByTaskId(taskId: String): Todo?
    suspend fun updateTask(taskId: String, map: Map<String, Any?>): ResultData<Boolean>
    suspend fun deleteTask(docId: String, taskImageLink: String?): ResultData<Boolean>
    suspend fun markTaskComplete(map: Map<String, Any?>, docId: String): Boolean

    // For user related
    suspend fun isUserAvailable(email: String): ResultData<String>
    suspend fun fetchTaskCreatorDetails(userId: String): User?
}