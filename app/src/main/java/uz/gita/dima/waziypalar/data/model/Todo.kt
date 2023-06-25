package uz.gita.dima.waziypalar.data.model

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val priority: String,
    val isComplete: Boolean = false
)
