package uz.gita.dima.waziypalar.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.dima.waziypalar.domain.usecase.TodoUseCase
import uz.gita.dima.waziypalar.domain.usecase.TodoUseCaseImpl


@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindTodoUseCase(impl: TodoUseCaseImpl): TodoUseCase
}