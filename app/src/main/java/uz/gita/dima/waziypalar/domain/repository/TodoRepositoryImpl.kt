package uz.gita.dima.waziypalar.domain.repository

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uz.gita.dima.waziypalar.data.model.Todo
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(): TodoRepository {
    private var  dbRef = FirebaseDatabase.getInstance().reference
    private val todoId = dbRef.push().key!!

    override fun addTodo(todo: Todo): Flow<Result<String>> = callbackFlow{
        dbRef.child(todoId).setValue(todo).addOnCompleteListener {
            trySend(Result.success("Success"))
        }.addOnFailureListener {
            trySend(Result.failure(it))
        }
        awaitClose()
    }
}