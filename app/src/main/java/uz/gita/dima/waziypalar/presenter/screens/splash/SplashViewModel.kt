package uz.gita.dima.waziypalar.presenter.screens.splash

import androidx.lifecycle.LiveData

interface SplashViewModel {
    val internetConnection: LiveData<Boolean>

    fun navigate()
}