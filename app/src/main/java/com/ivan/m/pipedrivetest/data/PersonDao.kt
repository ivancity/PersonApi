package com.ivan.m.pipedrivetest.data

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ivan.m.pipedrivetest.models.Person

@Dao
interface PersonDao {
    @Query("SELECT * FROM person")
    fun getAll(): List<Person>

    @Insert
    suspend fun insertAll(vararg persons: Person)

    @Query("DELETE FROM person")
    fun clearPersons()

    @Query("SELECT * FROM person")
    fun getPersons() : DataSource.Factory<Int, Person>
}