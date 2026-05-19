package com.example.rmp_frontend.data.mock

import com.example.rmp_frontend.domain.model.Transaction
import com.example.rmp_frontend.domain.repository.HistoryRepository
import kotlinx.coroutines.delay

class MockHistoryRepository : HistoryRepository {
    override suspend fun getTransactions(): List<Transaction> {
        delay(250)
        return MockData.transactions
    }
}
