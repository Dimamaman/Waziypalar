package uz.gita.dima.waziypalar.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.dima.waziypalar.presenter.screens.addedit.AddEditDirection
import uz.gita.dima.waziypalar.presenter.screens.addedit.AddEditDirectionImpl
import uz.gita.dima.waziypalar.presenter.screens.splash.SplashDirection
import uz.gita.dima.waziypalar.presenter.screens.splash.SplashDirectionImpl

@Module
@InstallIn(SingletonComponent::class)
interface DirectionModule {

    @Binds
    fun bindSplashDirection(impl: SplashDirectionImpl): SplashDirection

    @Binds
    fun bindAddEditDirection(impl: AddEditDirectionImpl): AddEditDirection
}