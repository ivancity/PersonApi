package com.ivan.m.pipedrivetest.models

import com.squareup.moshi.Json

data class Person(
    val id: Int,
    val name: String?,
    @Json(name = "org_name") var organizationName: String?,
    @Json(name = "owner_name") var ownerName: String?)