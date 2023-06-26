package uz.gita.dima.waziypalar.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import uz.gita.dima.waziypalar.model.Quote


interface ApiService {

    @Headers("x-rapidapi-key:1bf5a89813msh56794ecdecf4fddp147016jsnf16656420e1a")
    @GET("quote")
    suspend fun getQuote(): Quote
}