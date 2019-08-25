package com.ivan.m.pipedrivetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ivan.m.pipedrivetest.detail.ItemDetailFragment

import com.ivan.m.pipedrivetest.dummy.DummyContent
import com.ivan.m.pipedrivetest.persons.PersonListFragment
import com.ivan.m.pipedrivetest.persons.PersonViewModel
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list_content.view.*
import kotlinx.android.synthetic.main.person_list_layout.*

class MainActivity : AppCompatActivity() {

    private val personViewModel: PersonViewModel by lazy {
        ViewModelProviders.of(this).get(PersonViewModel::class.java)
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

        val goToDetailViewObserver = Observer<DummyContent.DummyItem> {this.setPersonDetailFragment(it)}
        personViewModel.showDetailView.observe(this, goToDetailViewObserver)

        val navigateToDetailsObserver = Observer<DummyContent.DummyItem> {this.navigateToDetails(it)}
        personViewModel.navigateToDetails.observe(this, navigateToDetailsObserver)
    }

    private fun setPersonListFragment() {
        val fragment = PersonListFragment.newInstance()
        goTo(fragment, R.id.person_list_container)
    }

    private fun setPersonDetailFragment(of: DummyContent.DummyItem) {
        val fragment = getDetailFragment(of)
        goTo(fragment, R.id.person_detail_container)
    }

    private fun navigateToDetails(of: DummyContent.DummyItem) {
        val fragment = getDetailFragment(of)
        val transaction = supportFragmentManager.beginTransaction().apply {
            replace(R.id.person_list_container, fragment)
            addToBackStack(null)
        }
        transaction.commit()
    }

    private fun getDetailFragment(of: DummyContent.DummyItem) : Fragment {
        return ItemDetailFragment().apply {
            arguments = Bundle().apply {
                putString(ItemDetailFragment.ARG_ITEM_ID, of.id)
            }
        }
    }

    private fun goTo(fragment: Fragment, withResLayout: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(withResLayout, fragment)
            .commit()
    }

    class SimpleItemRecyclerViewAdapter(
        private val viewModel: PersonViewModel,
        private val values: List<DummyContent.DummyItem>
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                viewModel.handlePersonItemClick(item)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
