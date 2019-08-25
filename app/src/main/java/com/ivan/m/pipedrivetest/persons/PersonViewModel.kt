package com.ivan.m.pipedrivetest.persons

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonViewModel : ViewModel() {

    private var twoPane: Boolean = false

    private val _setupList = MutableLiveData<Boolean>()
    val setupList: LiveData<Boolean> = _setupList

    private val _setupPersonRecycler = MutableLiveData<Boolean>()
    val setupRecycler: LiveData<Boolean> = _setupPersonRecycler

    fun initListView(detailContainer: View?) {
        setTwoPane(detailContainer)
        _setupList.value = true
    }

    fun initPersonListFragment() {
        _setupPersonRecycler.value = twoPane
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