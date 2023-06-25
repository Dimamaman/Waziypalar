package uz.gita.dima.waziypalar.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() : NavigationHandler, Navigator {
    override val navigationStack = MutableSharedFlow<NavigationArgs>()

    private suspend fun navigator(args: NavigationArgs) {
        navigationStack.emit(args)
    }

    override suspend fun back() = navigator {
        navigateUp()
    }

    override suspend fun replace(direction: AppScreen) = navigator {

    }

    override suspend fun replaceAll(direction: AppScreen) = navigator {

    }

    override suspend fun navigateTo(direction: AppScreen) = navigator {
        navigate(direction)
    }

    override suspend fun navigateTo(direction: List<AppScreen>) {

    }

    override suspend fun <T : AppScreen> backUntil(clazz: Class<T>) {

    }
}