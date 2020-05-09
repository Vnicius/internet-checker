package io.github.vnicius.internetchecker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import io.github.vnicius.internetchecker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        InternetChecker.liveDataIsInternetAvailable.observe(this) {
            viewBinding.internetState.text = if (it)
                "Yes"
            else
                "No"
        }
    }
}
