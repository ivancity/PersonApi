package com.ivan.m.pipedrivetest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.models.Phone

@Database(entities = [Person::class, Phone::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao() : PersonDao
    abstract fun phoneDao() : PhoneDao
}