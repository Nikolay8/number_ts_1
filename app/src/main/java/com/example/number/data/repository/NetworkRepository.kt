package com.example.number.data.repository


import com.example.number.data.network.Result
import okhttp3.ResponseBody

/**
 *  Repository for network functional
 */
interface NetworkRepository {

    suspend fun getNumberFact(number: Int): Result<ResponseBody>

    suspend fun getRandomFact(): Result<ResponseBody>

}
