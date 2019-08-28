package com.ivan.m.pipedrivetest.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.models.PersonsResponse
import com.ivan.m.pipedrivetest.services.PipeDriveApi
import kotlinx.coroutines.*

class PersonsListBoundaryCallback(private val pipeDriveApi: PipeDriveApi,
                                  private var nextStart: Int,
                                  private val pageSize: Int,
                                  private val callback: IncomingPersonsCallback) : PagedList.BoundaryCallback<Person>() {

    var isLoading = false
    private var moreItemInCollection = true

    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    override fun onZeroItemsLoaded() {
        dispatchPersonRequest()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Person) {
        if (isLoading) {
            return
        }

        if (!moreItemInCollection) {
            return
        }

        callback.showLoadingMore(true)
        isLoading = true
        dispatchPersonRequest()
    }


    private fun dispatchPersonRequest() = GlobalScope.launch(Dispatchers.Main) {
        val response = fetchPersons()
        if (!response.isSuccessful) {
            Log.e("BoundaryCallback", response.message())
            isLoading = false
            callback.showLoadingMore(false)
            handleError("We can't fetch data")
        } else {
            saveInDatabase(response.body())
        }
    }

    private fun handleError(message: String) {
        Log.e("BoundaryCallback", message)
        isLoading = false
        callback.showLoadingMore(false)
        _networkErrors.postValue(message)
    }

    private suspend fun fetchPersons() = withContext(Dispatchers.IO) {
        val personsResponse = pipeDriveApi.getPersons(nextStart, pageSize)
        personsResponse
    }

    private fun saveInDatabase(response: PersonsResponse?) {
        callback.showLoadingMore(false)
        if (response == null || response.data.isEmpty()) {
            return
        }
        val pagination = response.additional_data.pagination
        moreItemInCollection = pagination.moreItemsInCollection ?: false
        nextStart = pagination.nextStart ?: 0
        callback.handleResponse(response.data, nextStart, moreItemInCollection)
    }

}