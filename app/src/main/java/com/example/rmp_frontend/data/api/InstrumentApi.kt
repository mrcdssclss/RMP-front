package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.InstrumentDto
import com.example.rmp_frontend.data.dto.PricePointDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InstrumentApi {
    @GET("market/instruments")
    suspend fun getInstruments(): List<InstrumentDto>

    @GET("market/prices/history")
    suspend fun getPriceHistory(
        @Query("ticker") ticker: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("limit") limit: Int = 500,
    ): List<PricePointDto>
}
