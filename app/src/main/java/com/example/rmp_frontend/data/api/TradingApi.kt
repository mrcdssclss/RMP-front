package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.TradeRequestDto
import com.example.rmp_frontend.data.dto.TradeResultDto
import retrofit2.http.Body
import retrofit2.http.POST

interface TradingApi {
    @POST("orders/buy")
    suspend fun buy(@Body request: TradeRequestDto): TradeResultDto

    @POST("orders/sell")
    suspend fun sell(@Body request: TradeRequestDto): TradeResultDto
}
