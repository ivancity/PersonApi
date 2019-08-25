package com.ivan.m.pipedrivetest.persons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.ivan.m.pipedrivetest.MainActivity
import com.ivan.m.pipedrivetest.R
import com.ivan.m.pipedrivetest.dummy.DummyContent

import kotlinx.android.synthetic.main.person_list_fragment.*

class PersonListFragment : Fragment() {

    companion object {
        fun newInstance() = PersonListFragment()
    }

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
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter =
            MainActivity.SimpleItemRecyclerViewAdapter(personViewModel, DummyContent.ITEMS)
    }

}