package com.example.rmp_frontend.data

import com.example.rmp_frontend.model.Asset

class AssetRepository {

    fun getAssets(): List<Asset> {
        return listOf(
            Asset("Bitcoin", 102000.0, 4.2, 0.5, 95000.0),
            Asset("Ethereum", 4800.0, 2.1, 2.0, 4200.0),
            Asset("Tesla", 350.0, -1.2, 10.0, 400.0)
        )
    }
}