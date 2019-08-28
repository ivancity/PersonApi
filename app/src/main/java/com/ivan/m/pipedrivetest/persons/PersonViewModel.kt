package com.ivan.m.pipedrivetest.persons

import android.view.View
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.models.Phone
import com.ivan.m.pipedrivetest.repo.PersonRepository
import com.ivan.m.pipedrivetest.repo.RepoSearchResult
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: PersonRepository) : ViewModel() {

    private var twoPane: Boolean = false

    private lateinit var selectedPerson: Person

    private val _setupListView = MutableLiveData<Boolean>()
    val setupList: LiveData<Boolean> = _setupListView

    private val _setupPersonRecycler = MutableLiveData<Boolean>()
    val setupPersonRecycler: LiveData<Boolean> = _setupPersonRecycler

    private val _showDetailView = MutableLiveData<Person>()
    val showDetailView: LiveData<Person> = _showDetailView

    private val _navigateToDetails = MutableLiveData<Person>()
    val navigateToDetails: LiveData<Person> = _navigateToDetails

    private val _updateToolbarTitle = MutableLiveData<String>()
    val updateToolbarTitle: LiveData<String> = _updateToolbarTitle

    private val _updateDetailContent = MutableLiveData<Person>()
    val updateDetailContent: LiveData<Person> = _updateDetailContent

    // LiveData without Pagination
    private val _loadPersonsOnList = MutableLiveData<List<Person>>()

    // LiveData for Pagination
    private val _queryPersonLiveData = MutableLiveData<Boolean>()
    private val result: LiveData<RepoSearchResult> = Transformations.map(_queryPersonLiveData) {
        repository.setPersonsList()
    }
    val loadPersonList: LiveData<PagedList<Person>> = Transformations.switchMap(result) { it ->
        it.data }
    val networkErrors: LiveData<String> = Transformations.switchMap(result) { it ->
        it.networkErrors
    }

    val showLoading: LiveData<Boolean> = Transformations.map(repository.showLoading) {
        it
    }

    fun initListView(detailContainer: View?) {
        setTwoPane(detailContainer)
        _setupListView.value = true
    }

    fun initDetailView() {
        updateToolbarUi(selectedPerson)
        updateDetailsUi(selectedPerson)
    }

    fun initPersonListFragment() {
        _setupPersonRecycler.value = true

        // use this line if we want to use room, and fetch with pagination
        _queryPersonLiveData.value = true

        // use this line to test the api and only fetch data using coroutines. no database
//        fetchPersons()
    }

    fun handlePersonItemClick(using: Person?) {
        if (using == null) {
            return
        }

        selectedPerson = using

        if (twoPane) {
            _showDetailView.value = using
        } else {
            _navigateToDetails.value = using
        }
    }

    fun getPhoneStringFrom(phones: List<Phone>?) : String{
        if (phones == null) {
            return ""
        }

        if (phones.size == 1) {
            val item = phones.first()
            if (item.label == null && (item.value?.isEmpty() != false)) {
                return "No phones"
            }
        }

        var temp = ""
        for ((index, phone) in phones.withIndex()) {

            if (phone.value == null) {
                continue
            }
            temp += phone.value

            if (phone.label != null) {
                temp += " (" + phone.label + ")"
            }

            if (index < phones.size - 1) {
                temp += "\n"
            }

        }

        return temp
    }

    private fun setTwoPane(detailContainer: View?) {
        if (detailContainer != null) {
            // two panes will work if size is (res/values-w900dp).
            twoPane = true
        }
    }

    private fun updateDetailsUi(using: Person?) {
        if (using == null) {
            return
        }
        _updateDetailContent.value = using
    }

    private fun updateToolbarUi(using: Person?) {
        if (twoPane) {
            return
        }

        if (using == null) {
            // clear UI
            _updateToolbarTitle.value = ""
            return
        }

        _updateToolbarTitle.value = using.name
    }

    private fun fetchPersons() {
        viewModelScope.launch {
            val persons = repository.getPersons(0, 10)
            _loadPersonsOnList.value = persons
        }
    }

}