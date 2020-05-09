package io.github.vnicius.internetchecker

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import io.github.vnicius.internetchecker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val connectivityManager: ConnectivityManager by lazy {
        baseContext.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        InternetChecker.liveDataInternetState.observe(this) {
            viewBinding.internetState.text = when (it) {
                InternetState.AVAILABLE -> "Yes"
                InternetState.UNAVAILABLE -> "No"
            }
        }
    }
}
