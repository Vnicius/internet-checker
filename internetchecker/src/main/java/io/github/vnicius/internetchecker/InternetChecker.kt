package io.github.vnicius.internetchecker

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object InternetChecker : LifecycleObserver {
    private val mutableLiveDataInternetState = MutableLiveData<Boolean>().apply {
        value = false
    }
    private val connectivityCallback: ConnectivityManager.NetworkCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                availableNetworksList.add(network)
                updateConnectionState()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                availableNetworksList.remove(network)
                updateConnectionState()
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    updateConnectionState()
            }
        }
    }
    private var availableNetworksList: MutableSet<Network> = mutableSetOf()
    private lateinit var connectivityManager: ConnectivityManager

    val liveDataIsInternetAvailable: LiveData<Boolean> = mutableLiveDataInternetState
    val isInternetAvailable
        get() = liveDataIsInternetAvailable.value == true

    fun init(context: Context) {
        connectivityManager = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            connectivityCallback
        )
    }

    private fun updateConnectionState() {
        val hasInternetAvailable = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            availableNetworksList.any {
                connectivityManager.getNetworkCapabilities(it)
                    ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
            }
        } else {
            availableNetworksList.isNotEmpty()
        }

        if (hasInternetAvailable != liveDataIsInternetAvailable.value) {
            mutableLiveDataInternetState.postValue(hasInternetAvailable)
        }
    }
}