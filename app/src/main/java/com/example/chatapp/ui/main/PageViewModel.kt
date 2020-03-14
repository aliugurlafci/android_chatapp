package com.example.chatapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.ui.main.Sezar.SezarPost
import com.google.firebase.auth.*
import com.google.firebase.database.*

@IgnoreExtraProperties
data class MessageClass(
    val username: String? = "",
    val mesaj: String? = "",
    val metod: String? = "",
    val kim: String? = "",
    val userEmail: String? = ""
)

@IgnoreExtraProperties
data class User(
    val username: String? = "",
    val userEmail: String? = "",
    val lastLoginDate: String? = "",
    val password: String? = ""
)

class PageViewModel : ViewModel() {
    private var userList: MutableList<User?> = mutableListOf()
    private lateinit var referenceMessage: DatabaseReference
    private lateinit var referenceUser: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val _index = MutableLiveData<Int>()
    private var tempUser: User? = null

    //Firebase İşlemleri
    fun writeToMessageDatabase(
        username: String,
        mesaj: String,
        metod: String,
        kim: String,
        email: String
    ) {
        referenceMessage = FirebaseDatabase.getInstance().getReference("message")
        referenceMessage.child(referenceMessage.child(SezarPost(email).toSezar()).push().key!!)
            .setValue(
                MessageClass(
                    SezarPost(username).toSezar(),
                    SezarPost(mesaj).toSezar(),
                    SezarPost(metod).toSezar(),
                    SezarPost(kim).toSezar(),
                    SezarPost(email).toSezar()
                )
            )
    }

    fun getUser(email: String): User? {
        var user: User? = null
        referenceUser = FirebaseDatabase.getInstance().reference
        val listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child("user").children) {
                    if (item.key.equals(SezarPost(email).toSezar())) {
                        user = item.getValue(User::class.java)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceUser.addListenerForSingleValueEvent(listener)
        return user
    }

    fun writeToUserDatabase(
        user_name: String,
        user_email: String,
        last_login_date: String,
        pass: String
    ) {
        referenceUser = FirebaseDatabase.getInstance().getReference("user")
        referenceUser.child(SezarPost(user_email).toSezar()).setValue(
            User(
                username = SezarPost(user_name).toSezar(),
                userEmail = SezarPost(user_email).toSezar(),
                lastLoginDate = SezarPost(last_login_date).toSezar(),
                password = SezarPost(pass).toSezar()
            )
        )
    }

    fun getOnceCurrentMessageDataGet(childRef: String, token: String): MutableList<MessageClass?> {
        referenceMessage = FirebaseDatabase.getInstance().reference
        val liste: MutableList<MessageClass?> = mutableListOf()
        var temp: MessageClass? = null
        val listen = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child(childRef).children) {
                    temp = item.getValue(MessageClass::class.java)
                    if (temp != null) {
                        if (temp?.userEmail.equals(SezarPost(token).toSezar())) {
                            liste.add(temp)
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceMessage.addListenerForSingleValueEvent(listen)
        return liste
    }

    fun getCurrentMessageDataGet(childRef: String, username: String): MutableList<MessageClass?> {
        referenceMessage = FirebaseDatabase.getInstance().reference
        var liste: MutableList<MessageClass?> = mutableListOf()
        var temp: MessageClass? = null
        val listen = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child(childRef).children) {
                    temp = item.getValue(MessageClass::class.java)
                    if (temp != null) {
                        if (temp?.username.equals(username)) {
                            liste.add(temp)
                        }
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceMessage.addValueEventListener(listen)
        return liste
    }

    fun getUserData(childRef: String): MutableList<User?> {
        userList.clear()
        val currentUser = getUserUI().value
        referenceUser = FirebaseDatabase.getInstance().reference
        val listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child(childRef).children) {
                    tempUser = item.getValue(User::class.java)
                    if (tempUser != null && !tempUser?.username.equals(currentUser?.u_name)) {
                        userList.add(tempUser)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceUser.addListenerForSingleValueEvent(listener)
        return userList
    }

    /* fun getUserDataUsingValu(childRef: String,max:Int,min:Int):MutableList<User?>{
        userList.clear()
        referenceUser = FirebaseDatabase.getInstance().reference
        val listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for(item:DataSnapshot in p0.child(childRef).children.elementAt(min) .. p0.child(childRef).children.elementAt(max)){
                    tempUser = item.getValue(User::class.java)
                    if(tempUser!=null){
                        userList.add(tempUser)
                        tempUser=null
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceUser.addListenerForSingleValueEvent(listener)
        return userList
    }
*/
    fun setIndex(index: Int) {
        _index.value = index
    }

    companion object {
        var userUIList: MutableLiveData<userUI> = MutableLiveData<userUI>()
        var targetUIList: MutableLiveData<targetUI> = MutableLiveData<targetUI>()

        data class userUI(
            val u_name: String?,
            val u_mail: String?
        )

        data class targetUI(
            val u_name: String?,
            val u_mail: String?
        )

        fun addTargetUI(uName: String?, uMail: String?) {
            val r = targetUI(uName, uMail)
            targetUIList.value = r
        }

        fun getTargetUI(): MutableLiveData<targetUI> {
            return targetUIList
        }

        fun addUserUI(uName: String?, uMail: String?) {
            val t = userUI(uName, uMail)
            userUIList.value = t
        }

        fun getUserUI(): MutableLiveData<userUI> {
            return userUIList
        }
    }
}

