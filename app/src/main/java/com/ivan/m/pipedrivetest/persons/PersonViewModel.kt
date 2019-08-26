package com.ivan.m.pipedrivetest.persons

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivan.m.pipedrivetest.data.DatabaseService
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.models.Phone
import com.ivan.m.pipedrivetest.repo.PersonRepository
import com.ivan.m.pipedrivetest.services.ApiService
import kotlinx.coroutines.launch

class PersonViewModel(private val repository: PersonRepository) : ViewModel() {

    private var twoPane: Boolean = false

    lateinit var selectedPerson: Person

    private val _setupList = MutableLiveData<Boolean>()
    val setupList: LiveData<Boolean> = _setupList

    private val _setupPersonRecycler = MutableLiveData<List<Person>>()
    val setupPersonRecycler: LiveData<List<Person>> = _setupPersonRecycler

    private val _showDetailView = MutableLiveData<Person>()
    val showDetailView: LiveData<Person> = _showDetailView

    private val _navigateToDetails = MutableLiveData<Person>()
    val navigateToDetails: LiveData<Person> = _navigateToDetails

    private val _updateToolbarTitle = MutableLiveData<String>()
    val updateToolbarTitle: LiveData<String> = _updateToolbarTitle

    private val _updateDetailContent = MutableLiveData<Person>()
    val updateDetailContent: LiveData<Person> = _updateDetailContent

    private val _loadPersonsOnList = MutableLiveData<List<Person>>()
    val loadPersonsOnList: LiveData<List<Person>> = _loadPersonsOnList

    fun initListView(detailContainer: View?) {
        setTwoPane(detailContainer)
        _setupList.value = true
    }

    fun initDetailView(selectedId: String?) {
        updateToolbarUi(selectedPerson)
        updateDetailsUi(selectedPerson)
    }

    fun initPersonListFragment() {
        // list init no data so far
        _setupPersonRecycler.value = null

        fetchPersons()
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
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
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