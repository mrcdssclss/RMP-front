package com.example.rmp_frontend.domain.repository

import com.example.rmp_frontend.domain.model.Transaction

interface HistoryRepository {
    suspend fun getTransactions(): List<Transaction>
}
