package com.example.bluetootcontacttrace

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import database.DAtabaseHelper
import database.HelperBluetooth

class App : Application(){

    companion object {
        val CHANNEL_ID: String = "bound-service-channel"
    }

    override fun onCreate() {
        super.onCreate()
        DAtabaseHelper.initDatabaseInstance(this)
        HelperBluetooth.initDatabaseInstance(this)
        createNotificationChannel()
    }
    private fun createNotificationChannel() {

        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Bound Service Name",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.setSound(null, null)
        notificationChannel.importance = NotificationManager.IMPORTANCE_HIGH
        notificationChannel.enableLights(false)
        notificationChannel.enableVibration(false)
        notificationChannel.setShowBadge(false)


        val notificationManager : NotificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

}