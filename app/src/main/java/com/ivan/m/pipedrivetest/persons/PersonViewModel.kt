package com.ivan.m.pipedrivetest.persons

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivan.m.pipedrivetest.dummy.DummyContent

class PersonViewModel : ViewModel() {

    private var twoPane: Boolean = false

    private val _setupList = MutableLiveData<Boolean>()
    val setupList: LiveData<Boolean> = _setupList

    private val _setupPersonRecycler = MutableLiveData<Boolean>()
    val setupPersonRecycler: LiveData<Boolean> = _setupPersonRecycler

    private val _goToDetailView = MutableLiveData<DummyContent.DummyItem>()
    val goToDetailView: LiveData<DummyContent.DummyItem> = _goToDetailView

    private val _navigateToDetails = MutableLiveData<DummyContent.DummyItem>()
    val navigateToDetails: LiveData<DummyContent.DummyItem> = _navigateToDetails


    fun initListView(detailContainer: View?) {
        setTwoPane(detailContainer)
        _setupList.value = true
    }

    fun initPersonListFragment() {
        _setupPersonRecycler.value = twoPane
    }

    fun handlePersonItemClick(using: DummyContent.DummyItem?) {
        if (using == null) {
            return
        }

        if (twoPane) {
            _goToDetailView.value = using
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

}