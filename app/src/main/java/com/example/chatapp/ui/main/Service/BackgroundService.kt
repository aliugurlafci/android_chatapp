package com.example.chatapp.ui.main.Service

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.os.IBinder

class BackgroundService : Service() {

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun startService(service: Intent?): ComponentName? {
        return super.startService(service)
    }

    override fun stopService(name: Intent?): Boolean {
        return super.stopService(name)
    }
}
