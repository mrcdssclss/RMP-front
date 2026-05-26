package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.TransactionDto
import retrofit2.http.GET

interface HistoryApi {
    @GET("transactions")
    suspend fun getTransactions(): List<TransactionDto>
}
