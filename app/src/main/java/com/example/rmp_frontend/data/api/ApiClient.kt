package com.example.rmp_frontend.data.api

import com.example.rmp_frontend.data.storage.TokenStorage
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(
    baseUrl: String,
    tokenStorage: TokenStorage,
) {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = runBlocking { tokenStorage.getToken() }
            val requestBuilder = chain.request().newBuilder()
            if (token != null) {
                requestBuilder.header("Authorization", "${token.tokenType} ${token.accessToken}")
            }
            chain.proceed(requestBuilder.build())
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi = retrofit.create(AuthApi::class.java)
    val marketApi: MarketApi = retrofit.create(MarketApi::class.java)
    val instrumentApi: InstrumentApi = retrofit.create(InstrumentApi::class.java)
    val tradingApi: TradingApi = retrofit.create(TradingApi::class.java)
    val portfolioApi: PortfolioApi = retrofit.create(PortfolioApi::class.java)
    val historyApi: HistoryApi = retrofit.create(HistoryApi::class.java)
    val profileApi: ProfileApi = retrofit.create(ProfileApi::class.java)
}
