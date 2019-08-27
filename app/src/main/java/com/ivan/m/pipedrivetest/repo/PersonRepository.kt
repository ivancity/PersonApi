package com.ivan.m.pipedrivetest.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.ivan.m.pipedrivetest.data.AppDatabase
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.services.PipeDriveApi
import kotlinx.coroutines.*

class PersonRepository(private val pipeDriveApi: PipeDriveApi,
                       private val pipeDriveDb: AppDatabase) : IncomingPersonsCallback {

    private val pagedListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(5)
        .build()

    private var moreItemInCollection = true



    private var nextStart = 0

    private val _incomingPersons = MutableLiveData<PagedList<Person>>()
    var incomingPersons : LiveData<PagedList<Person>> = _incomingPersons

    override fun handleResponse(
        personResponse: List<Person>,
        nextStart: Int,
        moreItemInCollection: Boolean
    ) {
        this.moreItemInCollection = moreItemInCollection
        this.nextStart = nextStart
        dispatchPersonInsert(personResponse)
    }

    private fun dispatchPersonInsert(persons: List<Person>) = GlobalScope.launch(Dispatchers.Main) {
        insertToDatabase(persons)
    }

    private suspend fun insertToDatabase(persons: List<Person>) = withContext(Dispatchers.IO) {
        val personArray = persons.toTypedArray()
        pipeDriveDb.personDao().insertAll(*personArray)
    }

    override fun showLoadingMore(loadMore: Boolean) {

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
        val boundaryCallback = PersonsListBoundaryCallback(pipeDriveApi, nextStart, this)
        val networkErrors = boundaryCallback.networkErrors

        val data = LivePagedListBuilder(factory, pagedListConfig)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return RepoSearchResult(data, networkErrors)
    }

}