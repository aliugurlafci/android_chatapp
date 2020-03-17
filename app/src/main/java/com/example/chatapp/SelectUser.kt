package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.ui.main.Adaptor.HomeAdapter
import com.example.chatapp.ui.main.Adaptor.SelectUserAdapter
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_select_user.*
import kotlinx.android.synthetic.main.fragment_home.*

class SelectUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_user)

        val userSearch: EditText = findViewById(R.id.user_search)
        val userRecyclerView: RecyclerView = findViewById(R.id.user_recycler_view)
        val fabSearch: FloatingActionButton = findViewById(R.id.fab_userSearch)
        val currentUserData =PageViewModel.getUserUI().value
        val list: MutableList<User?> = PageViewModel().getUser("user", currentUserData?.u_name!!)
        //homeRecyclerView.adapter?.notifyDataSetChanged()
        user_recycler_view.adapter = SelectUserAdapter(list, applicationContext)
        user_recycler_view.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        user_recycler_view.visibility = View.VISIBLE


    }
}
