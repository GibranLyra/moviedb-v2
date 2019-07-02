package com.gibranlyra.moviedb.ui.component

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<MODEL, VH : BaseAdapter.ViewHolder<MODEL>,
        LISTENER : BaseAdapter.AdapterListener<MODEL>?>(val items: MutableList<MODEL>,
                                                        val listener: LISTENER?) : RecyclerView.Adapter<VH>() {

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    open fun add(items: MutableList<MODEL>, firstPage: Boolean = false) {
        with(this.items) {
            if (firstPage) {
                clear()
            }
            addAll(items)
        }
        notifyDataSetChanged()
    }

    abstract class ViewHolder<MODEL>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: MODEL)
    }

    interface AdapterListener<MODEL> {
        fun onAdapterItemClicked(position: Int, item: MODEL, view: View)
    }
}