package com.ivan.m.pipedrivetest.persons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivan.m.pipedrivetest.R
import com.ivan.m.pipedrivetest.models.Person
import kotlinx.android.synthetic.main.item_list_content.view.*

class PersonsRecyclerAdapter(
    private val viewModel: PersonViewModel
) :
    PagedListAdapter<Person, PersonsRecyclerAdapter.ViewHolder>(REPO_COMPARATOR) {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Person
            viewModel.handlePersonItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = getItem(position) ?: return

        holder.idView.text = value.id.toString()
        holder.contentView.text = value.name

        with(holder.itemView) {
            tag = value
            setOnClickListener(onClickListener)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean =
                oldItem == newItem
        }
    }

}