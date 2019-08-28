package com.ivan.m.pipedrivetest.models.db

import androidx.room.TypeConverter
import com.ivan.m.pipedrivetest.models.Phone
import com.ivan.m.pipedrivetest.services.ApiService
import com.squareup.moshi.Types

class Converters {
    @TypeConverter
    fun listToJson(value: List<Phone>?): String {
        val moshi = ApiService.provideMoshi()
        val type = Types.newParameterizedType(List::class.java, Phone::class.java)
        val adapter = moshi.adapter<List<Phone>>(type)
        return adapter.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<Phone>? {
        val moshi = ApiService.provideMoshi()
        val type = Types.newParameterizedType(List::class.java, Phone::class.java)
        val adapter = moshi.adapter<List<Phone>>(type)
        return adapter.fromJson(value)
    }
}