package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.dto.InstrumentDto
import retrofit2.http.GET

interface MarketApi {
    @GET("instruments")
    suspend fun getInstruments(): List<InstrumentDto>
}
