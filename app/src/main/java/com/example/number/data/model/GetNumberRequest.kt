package com.example.number.data.model

import com.squareup.moshi.JsonClass

/**
 * Request body for get number
 */
@JsonClass(generateAdapter = true)
data class GetNumberRequest(
    val number: Int
)
