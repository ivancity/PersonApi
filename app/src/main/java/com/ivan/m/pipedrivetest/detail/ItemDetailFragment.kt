package com.ivan.m.pipedrivetest.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ivan.m.pipedrivetest.MainActivity
import com.ivan.m.pipedrivetest.R
import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.persons.PersonViewModel
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

class ItemDetailFragment : Fragment() {

    private val personViewModel: PersonViewModel by lazy {
        activity?.run {
            ViewModelProviders.of(this).get(PersonViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)

        subscribe()

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                val item = it.getString(ARG_ITEM_ID)
                personViewModel.initDetailView(item)
            }
        }

        return rootView
    }

    private fun subscribe() {
        val updateToolbarObserver = Observer<String> {this.updateToolbar(it)}
        personViewModel.updateToolbarTitle.observe(this, updateToolbarObserver)

        val updateDetailContentObserver = Observer<Person> {this.updateContent(it)}
        personViewModel.updateDetailContent.observe(this, updateDetailContentObserver)
    }

    private fun updateContent(item: Person) {
        item_detail.text = item.organizationName ?: getString(R.string.no_organization)

        val phoneContent = personViewModel.getPhoneStringFrom(item.phone)
        item_detail_card_details_phone_content.text = phoneContent
    }

    private fun updateToolbar(withTitle: String) {
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
        (activity as MainActivity).supportActionBar?.title = withTitle
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
