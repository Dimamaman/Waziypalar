package uz.gita.dima.waziypalar.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.dima.waziypalar.navigation.NavigationDispatcher
import uz.gita.dima.waziypalar.navigation.NavigationHandler
import uz.gita.dima.waziypalar.navigation.Navigator

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun navigator(dispatcher: NavigationDispatcher): Navigator

    @Binds
    fun navigatorHandler(dispatcher: NavigationDispatcher): NavigationHandler

}