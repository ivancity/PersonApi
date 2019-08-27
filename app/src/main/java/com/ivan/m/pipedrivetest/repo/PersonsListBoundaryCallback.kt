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
                                  private val callback: IncomingPersonsCallback) : PagedList.BoundaryCallback<Person>() {

    var isLoading = false
    private var moreItemInCollection = true
    private var job: Job? = null

    private val _networkErrors = MutableLiveData<String>()
    val networkErrors: LiveData<String>
        get() = _networkErrors

    override fun onItemAtEndLoaded(itemAtEnd: Person) {
        if (isLoading) {
            return
        }

        if (!moreItemInCollection) {
            return
        }

        callback.showLoadingMore(true)
        isLoading = true


        job = dispatchPersonRequest()

//        dispatchPersonRequest2()
    }

    fun stopBoundaryCallback() {
        if (job == null) {
            return
        }
        job?.cancel()
    }


    private fun dispatchPersonRequest() = GlobalScope.launch(Dispatchers.Main) {
        val response = fetchPersons()
        if (!response.isSuccessful) {
            Log.e("BoundaryCallback", response.message())
            isLoading = false
            callback.showLoadingMore(false)
        } else {
            saveInDatabase(response.body())
        }
    }

    private fun dispatchPersonRequest2() = GlobalScope.launch(Dispatchers.Main) {
        try {
            val task = async(Dispatchers.IO) {
                val personsResponse = pipeDriveApi.getPersons(nextStart, 5)
                personsResponse
            }
            val response = withTimeoutOrNull(java.util.concurrent.TimeUnit.SECONDS.toMillis(200L)) {
                task.await()
            }

            if (response != null) {
                if (!response.isSuccessful) {
                    handleError(response.message())
                } else {
                    saveInDatabase(response.body())
                }
            } else {
                handleError("Coroutine timeout fetching persons")
            }
        } catch (e: Exception) {
            handleError(e.message ?: e.toString())
        }
    }

    private fun handleError(message: String) {
        Log.e("BoundaryCallback", message)
        isLoading = false
        callback.showLoadingMore(false)
        _networkErrors.postValue(message)
    }

    private suspend fun fetchPersons() = withContext(Dispatchers.IO) {
        val personsResponse = pipeDriveApi.getPersons(nextStart, 5)
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