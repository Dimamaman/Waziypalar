package uz.gita.dima.waziypalar.presenter.screens.splash


import uz.gita.dima.waziypalar.navigation.Navigator
import javax.inject.Inject

class SplashDirectionImpl @Inject  constructor(
    private val navigator: Navigator
): SplashDirection{
    override suspend fun navigateToMain() {
        navigator.navigateTo(SplashScreenDirections.actionSplashScreenToAddEditTodo())
    }
}