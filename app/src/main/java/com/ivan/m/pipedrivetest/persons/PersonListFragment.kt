package com.ivan.m.pipedrivetest.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.ivan.m.pipedrivetest.R
import com.ivan.m.pipedrivetest.models.Person

import kotlinx.android.synthetic.main.person_list_fragment.*

class PersonListFragment : Fragment() {

    companion object {
        fun newInstance() = PersonListFragment()
    }

    private lateinit var recyclerAdapter: PersonsRecyclerAdapter

    private val personViewModel: PersonViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this).get(PersonViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.person_list_fragment, container, false)

        subscribe()
        personViewModel.initPersonListFragment()

        return rootView
    }

    private fun subscribe() {
        val setupRecyclerView = Observer<Boolean> { this.setupRecyclerView(person_list_recycler) }
        personViewModel.setupPersonRecycler.observe(this, setupRecyclerView)

        val displayIncomingPersonObserver = Observer<PagedList<Person>> { displayPersons(it) }
        personViewModel.loadPersonList.observe(this, displayIncomingPersonObserver)

        personViewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(activity, "Wooops $it", Toast.LENGTH_LONG).show()
        })

        personViewModel.showLoading.observe(this, Observer<Boolean> {
            showLoading(it)
        })
    }

    private fun displayPersons(persons: PagedList<Person>) {
        recyclerAdapter.submitList(persons)
    }


    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerAdapter = PersonsRecyclerAdapter(personViewModel)
        recyclerView.adapter = recyclerAdapter
    }

    private fun showLoading(loading: Boolean) {
        // In a regular situation we will have a loading fragment dialog, and disable it if we
        // receive loading as false.
        if (loading) {
            Toast.makeText(activity, "Loading", Toast.LENGTH_LONG).show()
        }
    }
}