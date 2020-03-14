package com.example.chatapp.ui.main.Adaptor

import android.content.Intent
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.MainActivity
import com.example.chatapp.StartMessageActivity
import com.example.chatapp.ui.main.Fragment.HomeFragment
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Sezar.SezarGet
import com.example.chatapp.ui.main.User

class HomeAdapter(private val user: MutableList<User?>, var context: Context) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

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
        holder.userNameText.text = SezarGet(user[position]?.username).toMessage()
        holder.userEmailText.text = SezarGet(user[position]?.userEmail).toMessage()

        holder.startChat.setOnClickListener {
            PageViewModel.addTargetUI(user[position]?.username,user[position]?.userEmail)
            it.context.startActivity(Intent(it.context, StartMessageActivity::class.java))
        }
    }
}