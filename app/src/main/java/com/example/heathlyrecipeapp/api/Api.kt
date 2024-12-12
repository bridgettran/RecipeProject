package com.example.heathlyrecipeapp.api

/**
 * Provides network configuration and service for API communication.
 *
 * The `Api` object sets up a Retrofit instance with a base URL and Moshi for JSON serialization.
 * It exposes a `retrofitService` for accessing the `RecipesService`.
 */
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {

    /**
     * Base URL of the Edamam API.
     */
    private val BASE_URL = "https://api.edamam.com/"

    /**
     * Moshi instance with a Kotlin adapter for JSON parsing.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Retrofit instance configured with Moshi and the base URL.
     */
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    /**
     * Lazy initialization of the `RecipesService` for API calls.
     */
    val retrofitService: RecipesService by lazy {
        retrofit.create(RecipesService::class.java)
    }
}