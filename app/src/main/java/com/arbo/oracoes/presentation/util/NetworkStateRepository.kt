package com.arbo.oracoes.presentation.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import com.jakewharton.rxrelay2.BehaviorRelay

class NetworkStateRepository constructor(context: Context) {

    private var isOnline = false
    private var connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var callback = ConnectionStatusCallback()

    var connectionStatusEvent = BehaviorRelay.create<Boolean>()

    init {
        isOnline = getInitialConnectionStatus()
        sendEvent(isOnline)
        try {
            connectivityManager.unregisterNetworkCallback(callback)
        } catch (e: Exception) {
            Log.w(this.javaClass.name, "NetworkCallback for Wi-fi was not registered or already unregistered")
        }
        connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
    }

    private fun getInitialConnectionStatus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        } else {
            val activeNetwork = connectivityManager.getActiveNetworkInfo() // Deprecated in 29
            activeNetwork != null && activeNetwork.isConnectedOrConnecting // // Deprecated in 28
        }
    }

    private fun sendEvent(status: Boolean) {
        connectionStatusEvent.accept(status)
    }

    inner class ConnectionStatusCallback : ConnectivityManager.NetworkCallback() {

        private val activeNetworks: MutableList<Network> = mutableListOf()

        override fun onLost(network: Network) {
            super.onLost(network)
            activeNetworks.removeAll { activeNetwork -> activeNetwork == network }
            isOnline = activeNetworks.isNotEmpty()
            sendEvent(isOnline)
        }

        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            if (activeNetworks.none { activeNetwork -> activeNetwork == network }) {
                activeNetworks.add(network)
            }
            isOnline = activeNetworks.isNotEmpty()
            sendEvent(isOnline)
        }
    }
}