package com.example.heathlyrecipeapp.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinksX(
    @Json(name = "next")
    val next: Next
)