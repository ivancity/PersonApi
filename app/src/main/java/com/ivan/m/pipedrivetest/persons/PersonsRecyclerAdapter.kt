package com.ivan.m.pipedrivetest.persons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivan.m.pipedrivetest.R
import com.ivan.m.pipedrivetest.models.Person
import kotlinx.android.synthetic.main.item_list_content.view.*

class PersonsRecyclerAdapter(
    private val viewModel: PersonViewModel,
    private var values: List<Person>?
) :
    RecyclerView.Adapter<PersonsRecyclerAdapter.ViewHolder>() {

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
        if (values == null) {
            return
        }

        val dataCopy = values

        val item = dataCopy!![position]
        holder.idView.text = item.id.toString()
        holder.contentView.text = item.name

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values?.size ?: 0

    fun loadList(using: List<Person>) {
        values = using
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idView: TextView = view.id_text
        val contentView: TextView = view.content
    }
}