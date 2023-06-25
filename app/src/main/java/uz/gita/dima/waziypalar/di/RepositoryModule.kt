package uz.gita.dima.waziypalar.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.dima.waziypalar.domain.repository.TodoRepository
import uz.gita.dima.waziypalar.domain.repository.TodoRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindRepository(imp: TodoRepositoryImpl): TodoRepository
}