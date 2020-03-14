package com.example.chatapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.ui.main.Adaptor.ChatAdapter
import com.example.chatapp.ui.main.MessageClass
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Sezar.SezarGet
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_start_message.*

class StartMessageActivity : AppCompatActivity() {
    lateinit var listem: MutableList<MessageClass?>
    lateinit var referenceMessage: DatabaseReference
    var temp: MessageClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_message)

        referenceMessage = FirebaseDatabase.getInstance().getReference("message")
        val currentUI = PageViewModel.getTargetUI().value
        val recyclerView: RecyclerView = findViewById(R.id.chat_list_view)
        val senderBtn: Button = findViewById(R.id.senderBtn)
        val writedText: EditText = findViewById(R.id.sendText)
        listem = mutableListOf()
        val targetInfo = PageViewModel.getTargetUI()
        val userInfo = PageViewModel.getUserUI()
        val user1: TextView = findViewById(R.id.user1)
        user1.text = SezarGet(currentUI?.u_name.toString()).toMessage()
        val user2: TextView = findViewById(R.id.user2)
        user2.text = SezarGet(currentUI?.u_mail).toMessage()

        val CHANNEL_ID="CHANNEL"
        val notificationId=1
        listem = PageViewModel().getOnceCurrentMessageDataGet("message", targetInfo.value?.u_name!!)
        Handler().postDelayed({
            recyclerView.adapter = ChatAdapter(this, listem)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }, 1000)
        Handler().postDelayed({
            chat_list_view.smoothScrollToPosition(listem.size - 1)
        }, 1000)
        val cUser = PageViewModel.getUserUI().value
        senderBtn.setOnClickListener {
            val a: String = SezarGet(sezar = userInfo.value?.u_name!!).toMessage().toString()
            val b: String = writedText.text.toString()
            writedText.text = null
            val c = "post"
            val d: String = SezarGet(targetInfo.value?.u_name!!).toMessage().toString()
            val e: String = SezarGet(targetInfo.value?.u_mail!!).toMessage().toString()
            PageViewModel().writeToMessageDatabase(a, b, c, d, e)
        }
        //Notification will be here

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_email)
            .setContentTitle("LAN SALAK !")
            .setContentText("Biri mesaj attı")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "LAN SALAK !"
                val descriptionText = "Biri mesaj attı"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                    enableVibration(true)
                    enableLights(true)
                    lockscreenVisibility
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            val intent = Intent(this, StartMessageActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
        //Notifications end here
        val listen = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach { child ->
                    val message: MessageClass? = child.getValue(MessageClass::class.java)
                    with(NotificationManagerCompat.from(applicationContext)) {
                        // notificationId is a unique int for each notification that you must define
                        notify(notificationId, builder.build())
                        createNotificationChannel()
                    }
                    if (cUser?.u_name!!.equals(message?.kim) && !cUser?.u_name.equals(message?.username)) {
                        listem.add(
                            MessageClass(
                                message?.username,
                                message?.mesaj,
                                "get",
                                message?.kim,
                                message?.userEmail
                            )
                        )
                    } else {
                        listem.add(
                            MessageClass(
                                message?.username,
                                message?.mesaj,
                                "post",
                                message?.kim,
                                message?.userEmail
                            )
                        )
                    }
                    chat_list_view.adapter?.notifyItemInserted(listem.size - 1)
                    chat_list_view.smoothScrollToPosition(listem.size - 1)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        }
        referenceMessage.addValueEventListener(listen)
    }
}
