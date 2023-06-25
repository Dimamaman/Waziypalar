package uz.gita.dima.waziypalar.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import uz.gita.dima.waziypalar.data.todorepository.TodoRepositoryImp
import uz.gita.dima.waziypalar.domain.repository.TodoRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Binds
    fun bindTodoRepository(impl: TodoRepositoryImp): TodoRepository
}