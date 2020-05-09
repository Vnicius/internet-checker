package io.github.vnicius.internetchecker

import android.app.Application

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        InternetChecker.init(applicationContext)
    }
}