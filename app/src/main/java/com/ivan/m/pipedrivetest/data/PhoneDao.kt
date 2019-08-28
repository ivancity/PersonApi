package com.ivan.m.pipedrivetest.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivan.m.pipedrivetest.models.Phone

@Dao
interface PhoneDao {
    @Query("SELECT * FROM phone")
    fun getAll(): List<Phone>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg phones: Phone)

    @Query("DELETE FROM phone")
    fun clearPhone()
}