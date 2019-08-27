package com.ivan.m.pipedrivetest.models

import com.squareup.moshi.Json

data class Pagination(
    val start: Int,
    val limit: Int,
    @Json(name = "more_items_in_collection") var moreItemsInCollection: Boolean?,
    @Json(name = "next_start") var nextStart: Int?)