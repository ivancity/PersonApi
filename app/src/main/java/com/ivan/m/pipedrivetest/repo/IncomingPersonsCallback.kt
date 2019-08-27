package com.ivan.m.pipedrivetest.repo

import com.ivan.m.pipedrivetest.models.Person

interface IncomingPersonsCallback {
    fun handleResponse(personResponse: List<Person>, nextStart: Int, moreItemInCollection: Boolean)
    fun showLoadingMore(loadMore: Boolean)
}