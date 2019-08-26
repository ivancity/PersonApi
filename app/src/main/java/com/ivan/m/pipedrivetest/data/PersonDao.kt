package com.ivan.m.pipedrivetest.data

import androidx.room.Dao
import androidx.room.Query
import com.ivan.m.pipedrivetest.models.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): List<Person>
}