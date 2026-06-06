package com.iiinaiii.punks.data.remote.api.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkHandler(context: Context) {

    val connectivityManager = context.applicationContext
        .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isConnected: Boolean
        get() = isNetworkAvailable()

    private fun isNetworkAvailable(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
