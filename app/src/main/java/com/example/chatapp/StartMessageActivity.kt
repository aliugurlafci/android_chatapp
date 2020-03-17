package com.example.chatapp

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.ui.main.Adaptor.ChatAdapter
import com.example.chatapp.ui.main.MessageClass
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Sezar.SezarGet
import com.example.chatapp.ui.main.Sezar.SezarPost
import com.google.firebase.auth.UserInfo
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_start_message.*

class StartMessageActivity : AppCompatActivity() {
    lateinit var listem: MutableList<MessageClass?>
    lateinit var referenceMessage: DatabaseReference
    var ref = MutableLiveData<MessageClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_message)

        referenceMessage = FirebaseDatabase.getInstance().reference
        val currentUI = PageViewModel.getTargetUI().value
        val recyclerView: RecyclerView = findViewById(R.id.chat_list_view)
        val senderBtn: Button = findViewById(R.id.senderBtn)
        val writedText: EditText = findViewById(R.id.sendText)
        listem = mutableListOf()
        val targetInfo = PageViewModel.getTargetUI()
        val userInfo = PageViewModel.getUserUI()
        val userTyp: TextView = findViewById(R.id.user_typing)
        val user1: TextView = findViewById(R.id.user1)
        user1.text = SezarGet(currentUI?.u_name.toString()).toMessage()
        val user2: TextView = findViewById(R.id.user2)
        user2.text = SezarGet(currentUI?.u_mail).toMessage()

        listem = PageViewModel().getOnceCurrentMessageDataGet("message", userInfo.value?.u_name!!)
        Handler().postDelayed({
            recyclerView.adapter = ChatAdapter(this, listem)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }, 1000)
        Handler().postDelayed({
            chat_list_view.scrollToPosition(listem.size - 1)
        }, 1000)

        //Typing observer
        writedText.addTextChangedListener(object : TextWatcher {
            val user = PageViewModel.getTargetUI().value?.u_name!!
            override fun afterTextChanged(s: Editable?) {
                if(TextUtils.isEmpty(s.toString())){
                    PageViewModel().addTyping(user, false)
                    PageViewModel().getTyping(user)
                    user_typing.visibility=View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                PageViewModel().addTyping(user, true)
                PageViewModel().getTyping(user)
                user_typing.visibility=View.VISIBLE
            }

        })
        val cUser = PageViewModel.getUserUI().value
        senderBtn.setOnClickListener {

            val a: String = userInfo.value?.u_name!!.toString()
            val b: String = writedText.text.toString()
            writedText.text = null
            val c = "post"
            val d: String = SezarGet(targetInfo.value?.u_name!!.toString()).toMessage().toString()
            val e: String = SezarGet(targetInfo.value?.u_mail!!.toString()).toMessage().toString()

            PageViewModel().writeToMessageDatabase(a, b, c, d, e)
        }
        //Notification will be here
        ref.observe(this, Observer {
            listem.add(it)
            chat_list_view.adapter?.notifyDataSetChanged()

            chat_list_view.scrollToPosition(listem.size - 1)
        })

        //Notifications end here
        val listen = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child("message").children) {
                    val message: MessageClass? = item.getValue(MessageClass::class.java)
                    if (SezarPost(cUser?.u_name!!).toSezar().equals(message?.kim) && !SezarPost(
                            cUser?.u_name
                        ).toSezar().equals(message?.username)
                    ) {
                        val cls = MessageClass(
                            message?.username,
                            message?.mesaj,
                            "get",
                            message?.kim,
                            message?.userEmail
                        )
                        ref.value = cls
                    } else {
                        val cls = MessageClass(
                            message?.username,
                            message?.mesaj,
                            "post",
                            message?.kim,
                            message?.userEmail
                        )
                        ref.value = cls
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceMessage.addValueEventListener(listen)
    }
}
