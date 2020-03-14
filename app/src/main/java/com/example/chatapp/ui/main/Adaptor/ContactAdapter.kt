package com.example.chatapp.ui.main.Adaptor

import Contacts
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.ui.main.Control.Constant
import kotlinx.android.synthetic.main.home_recycler_view_item.view.*

class ContactAdapter(val list: ArrayList<Contacts>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    lateinit var mcontext: Context

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun addData(dataViews: ArrayList<Contacts>) {
        this.list.addAll(dataViews)
        notifyDataSetChanged()
    }

    fun getItemAtPosition(position: Int): Contacts {
        return list[position]
    }

    fun addLoadingView() {
        //Add loading item
        Handler().post {
            list.add(Contacts("",""))
            notifyItemInserted(list.size)
        }
    }

    fun removeLoadingView() {
        //Remove loading item
        if (list.size != 0) {
            list.removeAt(list.size-1)
            notifyItemRemoved(list.size-1)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mcontext = parent.context
        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_view_item, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(mcontext).inflate(R.layout.loading_data_view, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].name == "") {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            holder.itemView.person_name.text = list[position].name
            holder.itemView.person_email.text = list[position].number
        }
    }

}