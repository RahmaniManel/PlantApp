package com.example.planter_app.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.planter_app.MyApplication

object ConnectivityCheck {
    fun checkNetworkAvailability(): Boolean {
        val connectivityManager = MyApplication.instance!!.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val network = connectivityManager?.activeNetwork
            val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            val networkInfo = connectivityManager?.activeNetworkInfo
            networkInfo?.isConnected ?: false
        }
    }
}
