package com.example.number.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkService {

    @GET("/{NUMBER}")
    suspend fun getNumberFact(
        @Path("NUMBER") number: Int,
    ): Response<ResponseBody>

    @GET("/random/math")
    suspend fun getRandomFact(
    ): Response<ResponseBody>

}