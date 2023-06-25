package uz.gita.dima.waziypalar.presenter.screens.addedit

import uz.gita.dima.waziypalar.navigation.Navigator
import javax.inject.Inject

class AddEditDirectionImpl @Inject constructor(
    private val appNavigator: Navigator
): AddEditDirection {
    override suspend fun back() {
        appNavigator.back()
    }
}