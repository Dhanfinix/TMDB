package com.dhandev.myapp1

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPref : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPref = getSharedPreferences(LoginActivity.USER_DATA, MODE_PRIVATE)

        if (sharedPref.getString(LoginActivity.NAME, null) != null){
            MainActivity.open(this)
            finish()
        } else {
            LoginActivity.open(this)
            finish()
        }
    }
}