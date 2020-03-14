package com.example.chatapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.chatapp.ui.main.Database.UserDatabase
import com.example.chatapp.ui.main.Database.UserDb
import com.example.chatapp.ui.main.PageViewModel
import com.example.chatapp.ui.main.Sezar.SezarGet
import com.example.chatapp.ui.main.Sezar.SezarPost
import com.example.chatapp.ui.main.User

class LoginActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val login: Button = findViewById(R.id.login_user)
        val registerUser: Button = findViewById(R.id.register_user)
        val email: EditText = findViewById(R.id.login_email)
        val password: EditText = findViewById(R.id.login_password)

        val user = PageViewModel.getUserUI().value
        if (!TextUtils.isEmpty(user?.u_name) && !TextUtils.isEmpty(user?.u_mail)) goMain()

        registerUser.setOnClickListener {
            goRegister()
        }

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.VIBRATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.VIBRATE), 1)
        }

        login.setOnClickListener {
            var state :Boolean =false
            val list: MutableList<User?> = PageViewModel().getUserData("user")
            Handler().postDelayed({
                list.forEach {
                    if (it?.userEmail.equals(SezarPost(email.text.toString()).toSezar()) && it?.password.equals(SezarPost(password.text.toString()).toSezar())) {
                        PageViewModel.addUserUI(it?.username, it?.userEmail)
                        state=true
                        Toast.makeText(applicationContext, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                        goMain()
                    }
                }
            }, 500)
            /*
            if(!state){
                Toast.makeText(applicationContext, "Kaydınız bulunmuyor", Toast.LENGTH_SHORT).show()
                goRegister()
            }
            */

        }
    }

    fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun goRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
