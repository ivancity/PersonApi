package com.ivan.m.pipedrivetest.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.models.Phone
import com.ivan.m.pipedrivetest.models.db.Converters

@Database(entities = [Person::class, Phone::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun personDao() : PersonDao
    abstract fun phoneDao() : PhoneDao
}