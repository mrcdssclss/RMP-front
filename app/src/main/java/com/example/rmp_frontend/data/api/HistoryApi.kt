package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.TradeResultDto
import retrofit2.http.GET

interface HistoryApi {
    @GET("trades/history")
    suspend fun getTransactions(): List<TradeResultDto>
}
