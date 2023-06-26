package uz.gita.dima.waziypalar.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uz.gita.dima.waziypalar.data.network.FirebaseApi
import uz.gita.dima.waziypalar.model.Todo
import uz.gita.dima.waziypalar.model.User
import uz.gita.dima.waziypalar.utils.Constants.FEEDBACK_COLLECTION
import uz.gita.dima.waziypalar.utils.Constants.TASK_COLLECTION
import uz.gita.dima.waziypalar.utils.Constants.USER_COLLECTION
import uz.gita.dima.waziypalar.utils.ResultData
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** This is a repository class for all the network calls made to firebase */

@ExperimentalCoroutinesApi
class TodoRepository : FirebaseApi {

    val userDetails: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val db: FirebaseFirestore = Firebase.firestore
    private val storage = FirebaseStorage.getInstance().reference
    private val messaging = FirebaseMessaging.getInstance()

    // reference to fireStore tables
    private val taskListRef = db.collection(TASK_COLLECTION)
    private val userListRef = db.collection(USER_COLLECTION)

    // query to task table
    private val query: Query = taskListRef
        .whereEqualTo("creator", userDetails?.uid)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)

    private val assignedTaskQuery: Query = taskListRef
        .whereArrayContains("assignee", userDetails?.uid!!)
        .orderBy("todoCreationTimeStamp", Query.Direction.ASCENDING)


    override suspend fun saveUser(user: FirebaseUser) {
        val deviceToken = messaging.token.await()
        val userDetails = User(user.uid, user.email, user.displayName, deviceToken)
        userListRef.document(user.uid).set(userDetails, SetOptions.merge()).await()
    }


    override suspend fun createTask(taskItem: Todo): ResultData<Todo> =
        suspendCoroutine { cont ->
            taskListRef.add(taskItem)
                .addOnSuccessListener {
                    cont.resume(ResultData.Success(taskItem))
                }.addOnFailureListener {
                    cont.resume(ResultData.Failed(it.message))
                }
        }

    override suspend fun createTaskUsingWorker(taskMap: HashMap<*, *>): String {
        val document = taskListRef.add(taskMap).await()
        return document.id
    }


    override suspend fun fetchAllUnassignedTask(): Flow<MutableList<Todo>> = callbackFlow {
        val querySnapshot = query.addSnapshotListener { value, error ->
            if (error != null) {
                Log.d("BBB","Error -> $error")
                return@addSnapshotListener
            }
            else {
                value?.let { trySend(it.toObjects(Todo::class.java)) } ?: trySend(mutableListOf<Todo>())
            }
        }
        awaitClose { querySnapshot.remove() }
    }


    override suspend fun fetchOnlyAssignedTask(): Flow<MutableList<Todo>> = callbackFlow {
        val querySnapshot = assignedTaskQuery.addSnapshotListener { value, error ->
            if (error != null) {
                value?.let {
                    val a = it.toObjects(Todo::class.java)
                    Log.d("EEE","Assigned -> $a")
                }

                return@addSnapshotListener
            }
            else {
                value?.let {
                    trySend(it.toObjects(Todo::class.java))
                } ?: trySend(mutableListOf<Todo>())
            }
        }
        awaitClose { querySnapshot.remove() }
    }


    override suspend fun fetchTaskByTaskId(taskId: String): Todo? = try {
        val docSnapshot: DocumentSnapshot = taskListRef.document(taskId).get().await()
        if (docSnapshot.exists()) docSnapshot.toObject(Todo::class.java)
        else null
    } catch (e: Exception) {
        null
    }


    override suspend fun updateTask(taskId: String, map: Map<String, Any?>): ResultData<Boolean> =
        try {
            taskListRef.document(taskId).update(map).await()
            ResultData.Success(true)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }


    override suspend fun deleteTask(docId: String, taskImageLink: String?): ResultData<Boolean> =
        try {
            taskListRef.document(docId).delete().await()
            if (!taskImageLink.isNullOrEmpty()) storage.storage.getReferenceFromUrl(taskImageLink)
                .delete().await()
            ResultData.Success(true)
        } catch (e: Exception) {
            ResultData.Failed(e.message)
        }


    override suspend fun markTaskComplete(map: Map<String, Any?>, docId: String): Boolean = try {
        taskListRef.document(docId).update(map).await()
        true
    } catch (e: Exception) {
        false
    }


    override suspend fun isUserAvailable(email: String): ResultData<String> = try {
        ResultData.Loading
        val response = userListRef.whereEqualTo("email", email).get().await()
        val userId = response.toObjects(User::class.java)[0].uid

        Log.d("NNN","USER ID -> $userId,  $response")

        if (userId.isNotEmpty()) ResultData.Success(userId)
        else ResultData.Failed()
    } catch (e: Exception) {
        ResultData.Failed(e.message)
    }


    override suspend fun fetchTaskCreatorDetails(userId: String): User? = try {
        val response = userListRef.whereEqualTo("uid", userId).get().await()
        val userObj = response.toObjects(User::class.java)
        if (userId.isNotEmpty()) userObj[0]
        else null
    } catch (e: Exception) {
        null
    }
}