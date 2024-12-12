package com.example.heathlyrecipeapp.api.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "recipes")
@JsonClass(generateAdapter = true)
data class Recipe(
    @Json(name = "label")
    val label: String? = null,
    @Json(name = "calories")
    val calories: Double? = null,
    @Json(name = "totalTime")
    val totalTime: Double? = null,
    @Json(name = "healthLabels")
    val healthLabels: List<String>? = null,
    @Json(name = "image")
    val image: String? = null,
    @Json(name = "ingredientLines")
    val ingredientLines: List<String>? = null,
    @Json(name = "url")
    val url: String? = null,
    var isFavorite: Boolean? = false,

    // Define compositeKey as a field with backing property and mark it as the primary key
    @PrimaryKey(autoGenerate = false)
    val compositeKey: String = "${label}_${url?.hashCode()}"
)