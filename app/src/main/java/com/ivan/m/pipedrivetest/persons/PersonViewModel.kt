package com.ivan.m.pipedrivetest.persons

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.m.pipedrivetest.dummy.DummyContent
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.repo.PersonRepository
import com.ivan.m.pipedrivetest.services.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonViewModel : ViewModel() {

    private var twoPane: Boolean = false

    private var detailItem: DummyContent.DummyItem? = null

    private val repository: PersonRepository = PersonRepository(ApiService.pipeDriveApi)

    private val _setupList = MutableLiveData<Boolean>()
    val setupList: LiveData<Boolean> = _setupList

    private val _setupPersonRecycler = MutableLiveData<List<Person>>()
    val setupPersonRecycler: LiveData<List<Person>> = _setupPersonRecycler

    private val _showDetailView = MutableLiveData<DummyContent.DummyItem>()
    val showDetailView: LiveData<DummyContent.DummyItem> = _showDetailView

    private val _navigateToDetails = MutableLiveData<DummyContent.DummyItem>()
    val navigateToDetails: LiveData<DummyContent.DummyItem> = _navigateToDetails

    private val _updateToolbarTitle = MutableLiveData<String>()
    val updateToolbarTitle: LiveData<String> = _updateToolbarTitle

    private val _updateDetailContent = MutableLiveData<DummyContent.DummyItem>()
    val updateDetailContent: LiveData<DummyContent.DummyItem> = _updateDetailContent

    private val _loadPersonsOnList = MutableLiveData<List<Person>>()
    val loadPersonsOnList: LiveData<List<Person>> = _loadPersonsOnList

    fun initListView(detailContainer: View?) {
        setTwoPane(detailContainer)
        _setupList.value = true
    }

    fun initDetailView(using: DummyContent.DummyItem?) {
        detailItem = using
        updateToolbarUi(using)
        updateDetailsUi(using)
    }

    fun initPersonListFragment() {
        // list init no data so far
        _setupPersonRecycler.value = null

        fetchPersons()
    }

    fun handlePersonItemClick(using: DummyContent.DummyItem?) {
        if (using == null) {
            return
        }

        if (twoPane) {
            _showDetailView.value = using
        } else {
            _navigateToDetails.value = using
        }
    }

    private fun setTwoPane(detailContainer: View?) {
        if (detailContainer != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
    }

    private fun updateDetailsUi(using: DummyContent.DummyItem?) {
        if (using == null) {
            return
        }
        _updateDetailContent.value = using
    }

    private fun updateToolbarUi(using: DummyContent.DummyItem?) {
        if (twoPane) {
            return
        }

        if (using == null) {
            // clear UI
            _updateToolbarTitle.value = ""
            return
        }

        _updateToolbarTitle.value = using.content
    }

    private fun fetchPersons() {
        viewModelScope.launch {
            val persons = repository.getPersons(0, 10)
            _loadPersonsOnList.value = persons
        }
    }

}