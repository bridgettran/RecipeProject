package com.example.heathlyrecipeapp.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Hit(
    @Json(name = "_links")
    val links: Links,
    @Json(name = "recipe")
    val recipe: Recipe
)