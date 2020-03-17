package com.example.chatapp.ui.main.Adaptor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.StartMessageActivity
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Sezar.SezarGet
import com.example.chatapp.ui.main.User

class SelectUserAdapter(private val user: MutableList<User?>, var context: Context) :
    RecyclerView.Adapter<SelectUserAdapter.MyViewHolder>() {

    private lateinit var view: View

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameText: TextView = view.findViewById(R.id.person_name)
        val userEmailText: TextView = view.findViewById(R.id.person_email)
        val startChat: CardView = view.findViewById(R.id.start_chat_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recycler_view_item, parent, false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.userNameText.text = SezarGet(user.get(position)?.username).toMessage()
        holder.userEmailText.text = SezarGet(user.get(position)?.userEmail).toMessage()

        holder.startChat.setOnClickListener {
            it.context.startActivity(Intent(it.context, StartMessageActivity::class.java))
            val arr:ArrayList<String?> = arrayListOf(PageViewModel.getUserUI().value?.u_name,user[position]?.username)
            PageViewModel().createChannel("Channel 1",arr )
        }
    }
}