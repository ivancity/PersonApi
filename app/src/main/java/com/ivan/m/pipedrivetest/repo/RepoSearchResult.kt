package com.ivan.m.pipedrivetest.repo

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.ivan.m.pipedrivetest.models.Person

data class RepoSearchResult(
    val data: LiveData<PagedList<Person>>,
    val networkErrors: LiveData<String>)