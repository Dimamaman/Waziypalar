package uz.gita.dima.waziypalar.data.repository

import uz.gita.dima.waziypalar.data.network.ApiService
import javax.inject.Inject

/** This is a repository class for all the network calls made to RapidAPI server for qoute */

class QuoteRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun fetchQuote() = apiService.getQuote()
}