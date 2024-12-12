package com.example.heathlyrecipeapp.api.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeData(
    @Json(name = "count")
    val count: Int,
    @Json(name = "from")
    val from: Int,
    @Json(name = "hits")
    val hits: List<Hit>,
    @Json(name = "_links")
    val links: LinksX,
    @Json(name = "to")
    val to: Int
)