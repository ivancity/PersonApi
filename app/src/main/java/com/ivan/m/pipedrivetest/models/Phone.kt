package com.ivan.m.pipedrivetest.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Phone(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    var label: String?,
    var value: String?,
    var ownerId: Int?)