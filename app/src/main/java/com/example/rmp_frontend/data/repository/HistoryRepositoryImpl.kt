package com.example.rmp_frontend.data.repository

import com.example.rmp_frontend.data.api.HistoryApi
import com.example.rmp_frontend.data.mapper.toDomain
import com.example.rmp_frontend.domain.model.Transaction
import com.example.rmp_frontend.domain.repository.HistoryRepository

class HistoryRepositoryImpl(
    private val historyApi: HistoryApi,
) : HistoryRepository {
    override suspend fun getTransactions(): List<Transaction> {
        return historyApi.getTransactions().map { it.toDomain() }
    }
}
