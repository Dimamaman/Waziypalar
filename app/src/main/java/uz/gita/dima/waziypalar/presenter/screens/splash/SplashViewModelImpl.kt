package uz.gita.dima.waziypalar.presenter.screens.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.dima.waziypalar.util.hasConnection
import javax.inject.Inject

@HiltViewModel
class SplashViewModelImpl @Inject constructor(
    private val direction: SplashDirection
): ViewModel(), SplashViewModel {
    override val internetConnection = MutableLiveData<Boolean>()

    override fun navigate() {
        if (hasConnection()) {
            internetConnection.value = true
            viewModelScope.launch {
                delay(2000L)
                direction.navigateToMain()
            }
        } else {
            internetConnection.value = false
        }
    }
}