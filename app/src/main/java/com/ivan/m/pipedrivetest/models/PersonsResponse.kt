package com.ivan.m.pipedrivetest.models

data class PersonsResponse(
    val success: Boolean,
    val data: List<Person>,
    val additional_data: AdditionalData
)
