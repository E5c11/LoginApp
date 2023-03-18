package com.test.loginapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.provider.Settings
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class NetworkTracker @Inject constructor(private val context: Context) {

    private val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks = mutableSetOf<Network>()
    private var emitInitialState = false
    private var networkState = false

    fun getNetworkState(): Flow<NetworkStatus> = merge(
        // initial flow
        flow<NetworkStatus> {
            val netCapabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if (netCapabilities == null && !emitInitialState) emit(NetworkStatus.Unavailable)
            networkState = netCapabilities != null
            emitInitialState = true
        }.flowOn(Dispatchers.IO),
        // switch to continuous flow
        networkStatus
    )


    private val networkStatus = callbackFlow<NetworkStatus> {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                val netCap: NetworkCapabilities? = cm.getNetworkCapabilities(network)
                val internet = netCap?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
                if (internet) validNetworks.add(network)
                val state = checkValidNetworks()
                if (state != networkState && state) trySend(NetworkStatus.Available).isSuccess
                networkState = state
            }

            override fun onLost(network: Network) {
                validNetworks.remove(network)
                val state = checkValidNetworks()
                if (state != networkState && !state) trySend(NetworkStatus.Unavailable).isSuccess
                networkState = state
            }
        }
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        cm.registerNetworkCallback(request, networkStatusCallback)

        awaitClose {
            cm.unregisterNetworkCallback(networkStatusCallback)
        }
}

    fun checkValidNetworks(): Boolean {
        val netAvailable: Boolean = validNetworks.size > 0
        var netValid: Boolean
        if (netAvailable) {
            try {
                val urlc = URL("https://clients3.google.com/generate_204")
                    .openConnection() as HttpURLConnection
                urlc.setRequestProperty("User-Agent", "Android")
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 1500
                urlc.connect()
                netValid = urlc.responseCode == 204 && urlc.contentLength == 0
            } catch (e: IOException) {
                netValid = false
            }
        } else netValid = false

        return netValid
    }
}

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable : NetworkStatus()
}