package com.example.chatapp.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Ignore
import com.example.chatapp.ui.main.Sezar.SezarPost
import com.google.firebase.auth.*
import com.google.firebase.database.*
import kotlinx.coroutines.flow.emptyFlow

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
@IgnoreExtraProperties

data class Channel(
    val channelName: String? = "",
    val channelUser: ArrayList<String?> = arrayListOf()
)

class PageViewModel : ViewModel() {
    private lateinit var referenceMessage: DatabaseReference
    private lateinit var referenceUser: DatabaseReference
    private lateinit var typingReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val _index = MutableLiveData<Int>()
    val typingState = MutableLiveData<Boolean>()
    private lateinit var referenceChannel: DatabaseReference

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

    fun createChannel(channelName: String, channelUser: ArrayList<String?>) {
        val ch = Channel(channelName, channelUser)
        referenceChannel = FirebaseDatabase.getInstance().getReference("channel")
        referenceChannel.child(referenceChannel.child(channelName).push().key!!).setValue(ch)

    }

    fun getChannel(username: String?):MutableList<Channel?> {
        referenceChannel = FirebaseDatabase.getInstance().reference
        val list: MutableList<Channel?> = mutableListOf()
        val listen = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child("channel").children) {
                    val temp = item.getValue(Channel::class.java)
                    if(temp!!.channelUser.contains(username)){
                        list.add(temp)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceChannel.addListenerForSingleValueEvent(listen)
        return list
    }

    fun addTyping(username: String,isTyping: Boolean) {
        typingReference = FirebaseDatabase.getInstance().getReference("typing")
        val tp = typingClass(username, isTyping)
        typingReference.child(username).setValue(tp)
    }
    fun getTyping(username: String?) {
        typingReference = FirebaseDatabase.getInstance().reference
        val listen = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child("typing").children) {
                    val temp = item.getValue(typingClass::class.java)
                    if (temp?.username!!.equals(username)) {
                        typingState.value = temp?.isTyping
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        typingReference.addListenerForSingleValueEvent(listen)
    }

    fun getChannel(): MutableList<Channel?> {
        val channelID: DatabaseReference = FirebaseDatabase.getInstance().reference
        val list: MutableList<Channel?> = mutableListOf()
        var temp: Channel? = null
        val currentUI = getUserUI().value?.u_name.toString()
        val listener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child("channel").children) {
                    temp = item.getValue(Channel::class.java)
                    if (temp!!.channelUser.contains(currentUI)) {
                        list.add(temp)
                    }
                }
            }
        }
        referenceChannel.addListenerForSingleValueEvent(listener)
        return list
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
        liste.clear()
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

    fun getUserData(childRef: String, u_email: String, u_pass: String): MutableList<User?> {
        val userList: MutableList<User?> = mutableListOf()
        referenceUser = FirebaseDatabase.getInstance().reference

        val listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child(childRef).children) {
                    val tempUser = item.getValue(User::class.java)
                    if (tempUser?.userEmail.toString()
                            .equals(SezarPost(u_email).toSezar()) && tempUser?.password.toString()
                            .equals(SezarPost(u_pass).toSezar())
                    ) {
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

    fun getUser(childRef: String, username: String): MutableList<User?> {
        val userList: MutableList<User?> = mutableListOf()
        referenceUser = FirebaseDatabase.getInstance().reference

        val listener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (item in p0.child(childRef).children) {
                    val tempUser = item.getValue(User::class.java)
                    if (!tempUser?.username.toString().equals(SezarPost(username).toSezar())) {
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

    fun setIndex(index: Int) {
        _index.value = index
    }

    companion object {
        /**
         * giriş yapan kullanıcının bilgilerini tutan liste
         */
        private var userUIList: MutableLiveData<userUI> = MutableLiveData<userUI>()
        private var targetUIList: MutableLiveData<targetUI> = MutableLiveData<targetUI>()

        data class userUI(
            val u_name: String?,
            val u_mail: String?
        )

        data class targetUI(
            val u_name: String?,
            val u_mail: String?
        )

        data class typingClass(
            val username: String?,
            val isTyping: Boolean
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

