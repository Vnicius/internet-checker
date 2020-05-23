package io.github.vnicius.internetchecker

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object InternetChecker : LifecycleObserver {
    private const val PING_DELAY = 5000L
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
    private lateinit var pingHelper: PingHelper
    private val pingHandler = Handler()
    private val pingProcess = object : Runnable {
        override fun run() {
            try {
                pingHandler.removeCallbacks(this)

                if (::pingHelper.isInitialized) {
                    pingHelper.ping(onError = {
                        availableNetworksList.clear()
                        updateConnectionState()
                    })
                }

                pingHandler.postDelayed(this, pingDelay)
            } catch (e: Exception) {
            }
        }
    }
    private var availableNetworksList: MutableSet<Network> = mutableSetOf()
    private lateinit var connectivityManager: ConnectivityManager
    private var pingDelay: Long = PING_DELAY

    val liveDataIsInternetAvailable: LiveData<Boolean> = mutableLiveDataInternetState
    val isInternetAvailable
        get() = liveDataIsInternetAvailable.value == true

    fun init(context: Context, pingDelay: Long = PING_DELAY) {
        this.pingDelay = pingDelay

        connectivityManager = context.applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            connectivityCallback
        )

        pingHelper = PingHelper(context.applicationContext)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pingHandler.post(pingProcess)
        }
    }

    fun disablePing() = pingHandler.removeCallbacks(pingProcess)

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