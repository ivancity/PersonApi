package com.ivan.m.pipedrivetest.services

import com.ivan.m.pipedrivetest.models.PersonsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PipeDriveApi {
    @GET("persons?")
    suspend fun getPersons(@Query("start") start: Int,
                   @Query("limit") limit: Int): Response<PersonsResponse>
}