package uz.gita.dima.waziypalar.util

sealed class ResultData<out T> {
    object DoNothing : ResultData<Nothing>()
    object Loading : ResultData<Nothing>()
    data class Success<out T>(val data: T? = null) : ResultData<T>()
    data class Failed(val message: String? = null) : ResultData<Nothing>()
}