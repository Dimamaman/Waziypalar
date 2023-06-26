package uz.gita.dima.waziypalar.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.dima.waziypalar.data.network.ApiService
import uz.gita.dima.waziypalar.data.repository.QuoteRepository
import uz.gita.dima.waziypalar.data.repository.TodoRepository
import uz.gita.dima.waziypalar.utils.Constants.QUOTE_API
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/** [NetworkModule] provides dependecies through application level injections, specifically for
 * network calls
 * */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(
        converterFactory: Converter.Factory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(QUOTE_API)
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideQuoteRepository(apiService: ApiService) = QuoteRepository(apiService)

    @ExperimentalCoroutinesApi
    @Provides
    fun provideTodoRepository() = TodoRepository()
}