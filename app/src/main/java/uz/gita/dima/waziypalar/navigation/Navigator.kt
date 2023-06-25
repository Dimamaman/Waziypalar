package uz.gita.dima.waziypalar.navigation

import androidx.navigation.NavDirections

typealias AppScreen = NavDirections

interface Navigator {
    suspend fun back()
    suspend fun replace(direction: AppScreen)
    suspend fun replaceAll(direction: AppScreen)
    suspend fun navigateTo(direction: AppScreen)
    suspend fun navigateTo(direction: List<AppScreen>)
    suspend fun <T : AppScreen> backUntil(clazz: Class<T>)
}