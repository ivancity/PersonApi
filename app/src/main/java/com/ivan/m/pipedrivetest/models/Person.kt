package com.ivan.m.pipedrivetest.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Person(
    @PrimaryKey var id: Int,
    var name: String?,
    @ColumnInfo(name = "org_name") @Json(name = "org_name") var organizationName: String?,
    @ColumnInfo(name = "owner_name") @Json(name = "owner_name") var ownerName: String?,
    @Ignore var phone: List<Phone>? = null) {
    constructor() : this(0, "", "", "", null)
}