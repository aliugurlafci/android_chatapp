package com.example.chatapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.chatapp.ui.main.PageViewModel
import kotlinx.android.synthetic.main.activity_register.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RegisterActivity : AppCompatActivity() {

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val regUsername: EditText = findViewById(R.id.register_username)
        val regEmail: EditText = findViewById(R.id.register_email)
        val regPass: EditText = findViewById(R.id.register_password)
        val regPassAgain: EditText = findViewById(R.id.register_password_again)
        val signUp: Button = findViewById(R.id.register_user_in)
        val goBack: ImageView = findViewById(R.id.goback_icon)

        goBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        signUp.setOnClickListener {

            if (!TextUtils.isEmpty(regUsername.text) && !TextUtils.isEmpty(regEmail.text) && !TextUtils.isEmpty(
                    regPass.text
                ) && !TextUtils.isEmpty(regPassAgain.text)
            ) {
                if (regEmail.text.contains('@') && regEmail.text.contains(".com")) {
                    if (regPass.text.toString().equals(regPassAgain.text.toString())) {
                        if (regPass.text.length >= 6) {
                            PageViewModel().writeToUserDatabase(
                                regUsername.text.toString(),
                                regEmail.text.toString(),
                                "LocalDateTime.now().format(formatter)",
                                regPass.text.toString()
                            )
                            PageViewModel().writeToMessageDatabase(
                                regUsername.text.toString(),
                                "Merhaba",
                                "get",
                                "Sistem",
                                register_email.text.toString()
                            )
                            Toast.makeText(
                                applicationContext,
                                "Kaydınız başarıyla oluşturuldu",
                                Toast.LENGTH_SHORT
                            ).show()
                            Handler().postDelayed({
                                startActivity(Intent(this, LoginActivity::class.java))
                            }, 400)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Şifre uzunlugu 6 karakterden az olamaz !",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Şifreniz eşleşmiyor!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "E-Mail alanını kontrol edip tekrar deneyiniz !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Gerekli alanları doldurunz !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
