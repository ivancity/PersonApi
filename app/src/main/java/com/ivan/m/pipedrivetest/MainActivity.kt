package com.ivan.m.pipedrivetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.ivan.m.pipedrivetest.data.DatabaseService
import com.ivan.m.pipedrivetest.detail.ItemDetailFragment

import com.ivan.m.pipedrivetest.models.Person
import com.ivan.m.pipedrivetest.persons.PersonListFragment
import com.ivan.m.pipedrivetest.persons.PersonViewModel
import com.ivan.m.pipedrivetest.persons.PersonViewModelFactory
import com.ivan.m.pipedrivetest.repo.PersonRepository
import com.ivan.m.pipedrivetest.services.ApiService
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.person_list_layout.*

class MainActivity : AppCompatActivity() {

    private val personViewModel: PersonViewModel by lazy {
        val repo = PersonRepository(ApiService.pipeDriveApi, DatabaseService.getDatbase(this))
        val personViewModelFactory = PersonViewModelFactory(repo)
        ViewModelProvider(this, personViewModelFactory)
            .get(PersonViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        subscribe()
        personViewModel.initListView(person_detail_container)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.title = title
        return true
    }

    private fun subscribe() {
        val setupListObserver = Observer<Boolean> {this.setPersonListFragment()}
        personViewModel.setupList.observe(this, setupListObserver)

        val goToDetailViewObserver = Observer<Person> {this.setPersonDetailFragment(it)}
        personViewModel.showDetailView.observe(this, goToDetailViewObserver)

        val navigateToDetailsObserver = Observer<Person> {this.navigateToDetails(it)}
        personViewModel.navigateToDetails.observe(this, navigateToDetailsObserver)
    }

    private fun setPersonListFragment() {
        val fragment = PersonListFragment.newInstance()
        goTo(fragment, R.id.person_list_container)
    }

    private fun setPersonDetailFragment(of: Person) {
        val fragment = getDetailFragment(of)
        goTo(fragment, R.id.person_detail_container)
    }

    private fun navigateToDetails(of: Person) {
        val fragment = getDetailFragment(of)
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(R.id.person_list_container, fragment)
            addToBackStack(null)
        }
        transaction.commit()
    }

    private fun getDetailFragment(of: Person) : Fragment {
        return ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ItemDetailFragment.ARG_ITEM_ID, of.id.toString())
            }
        }
    }

    private fun goTo(fragment: Fragment, withResLayout: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(withResLayout, fragment)
            .commit()
    }
}
