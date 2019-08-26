package com.ivan.m.pipedrivetest.models

import androidx.room.Entity

@Entity
data class Phone(
    var label: String?,
    var value: String?,
    var ownerId: Int?)