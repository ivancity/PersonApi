package com.ivan.m.pipedrivetest.repo

import com.ivan.m.pipedrivetest.data.AppDatabase
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.services.PipeDriveApi

class PersonRepository(private val pipeDriveApi: PipeDriveApi,
                       private val pipeDriveDb: AppDatabase) {

    suspend fun getPersons(page: Int, limit: Int) : List<Person>? {
        val personsResponse = pipeDriveApi.getPersons(page, limit)

        if (!personsResponse.isSuccessful) {
            return null
        }

        return personsResponse.body()?.data
    }

}