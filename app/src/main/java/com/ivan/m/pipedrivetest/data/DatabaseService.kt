package com.ivan.m.pipedrivetest.data

import android.content.Context
import androidx.room.Room

object DatabaseService {
    fun getDatabase(context: Context) : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "pipedrive-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}