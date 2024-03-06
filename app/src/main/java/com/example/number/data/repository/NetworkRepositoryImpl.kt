package com.example.number.data.repository

import com.example.number.data.api.NetworkService
import com.example.number.data.network.RestAPIClient
import com.example.number.data.network.Result
import okhttp3.ResponseBody

class NetworkRepositoryImpl(
    private val networkService: NetworkService
) : NetworkRepository {
    override suspend fun getNumberFact(number: Int): Result<ResponseBody> {
        return RestAPIClient.callAPI("getNumberFact") {
            networkService.getNumberFact(number)
        }
    }

    override suspend fun getRandomFact(): Result<ResponseBody> {
        return RestAPIClient.callAPI("getRandomFact") {
            networkService.getRandomFact()
        }
    }
}
