package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.InstrumentDto
import com.example.rmp_frontend.data.dto.PricePointDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InstrumentApi {
    @GET("instruments/{id}")
    suspend fun getInstrument(@Path("id") instrumentId: String): InstrumentDto

    @GET("instruments/{id}/history")
    suspend fun getPriceHistory(
        @Path("id") instrumentId: String,
        @Query("period") period: String,
    ): List<PricePointDto>
}
