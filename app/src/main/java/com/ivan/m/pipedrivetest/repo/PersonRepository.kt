package com.ivan.m.pipedrivetest.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import com.ivan.m.pipedrivetest.data.AppDatabase
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.services.PipeDriveApi
import kotlinx.coroutines.*

class PersonRepository(private val pipeDriveApi: PipeDriveApi,
                       private val pipeDriveDb: AppDatabase) : IncomingPersonsCallback {

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean> = _showLoading

    override fun showLoadingMore(loadMore: Boolean) {
        _showLoading.value = loadMore
    }

    override fun handleResponse(
        personResponse: List<Person>,
        nextStart: Int,
        moreItemInCollection: Boolean
    ) {
        dispatchPersonInsert(personResponse)
    }

    private fun dispatchPersonInsert(persons: List<Person>) = GlobalScope.launch(Dispatchers.Main) {
        insertToDatabase(persons)
    }

    private suspend fun insertToDatabase(persons: List<Person>) = withContext(Dispatchers.IO) {
        val personArray = persons.toTypedArray()
        pipeDriveDb.personDao().insertAll(*personArray)
    }

    suspend fun getPersons(page: Int, limit: Int) : List<Person>? {
        val personsResponse = pipeDriveApi.getPersons(page, limit)

        if (!personsResponse.isSuccessful) {
            return null
        }

        return personsResponse.body()?.data
    }

    fun setPersonsList() : RepoSearchResult{
        val factory = pipeDriveDb.personDao().getPersons()
        val boundaryCallback = PersonsListBoundaryCallback(
            pipeDriveApi,
            0,
            PAGE_SIZE,
            this)
        val networkErrors = boundaryCallback.networkErrors

        val data = LivePagedListBuilder(factory, PAGE_SIZE)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return RepoSearchResult(data, networkErrors)
    }

    companion object {
        const val PAGE_SIZE = 10
    }
}