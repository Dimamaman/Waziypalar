package uz.gita.dima.waziypalar.model

import com.google.firebase.firestore.DocumentId

data class Todo(
    @DocumentId
    var docId: String = "",
    var creator: String = "",
    var todoBody: String = "",
    var todoDesc: String? = "",
    var todoDate: String? = "",
    var todoCreationTimeStamp: String = "",
    @JvmField
    var isCompleted: Boolean = false,
    var taskImage: String? = "",
    var assignee: List<*>? = null,
    var priority: Int? = 0,
)