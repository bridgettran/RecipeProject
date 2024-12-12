package com.example.heathlyrecipeapp.utility

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class Converters {
    private val moshi = Moshi.Builder().build()

    @TypeConverter
    fun fromString(value: String?): List<String>?{
        if (value == null) return null

        val listType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(listType)

        return adapter.fromJson(value)
    }

    @TypeConverter
    fun ListToString(list: List<String>?): String?{
        if (list == null) return null

        val listType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(listType)

        return adapter.toJson(list)
    }
}