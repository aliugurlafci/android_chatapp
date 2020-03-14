package com.example.chatapp.ui.main.Auth

import com.google.firebase.auth.FirebaseAuth

class LoginControl(val email:String,val password:String){
    private lateinit var auth:FirebaseAuth
    fun createUser():Boolean{
        var state:Boolean=false
        auth=FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            state = task.isSuccessful
        }
        return state
    }

    fun signUser():Boolean{
        auth=FirebaseAuth.getInstance()
        var state=false
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            state = task.isSuccessful
        }
        return state
    }
}