package com.dhandev.myapp1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dhandev.myapp1.databinding.ActivityLoginBinding
import com.dhandev.myapp1.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences(USER_DATA, MODE_PRIVATE)
        val editor = sharedPref.edit()


        binding.btnLogin.setOnClickListener {
            val name = binding.etName.text.trim().toString()
            val password = binding.etPass.text.trim().toString()

            if (validate(name, password)){
                editor.putString(NAME, name)
                editor.putString(PASSWORD, password)
                editor.putBoolean(IS_LOGIN, true)
                editor.apply()
                MainActivity.open(this)
                finish()
            }
        }
    }

    private fun validate(name: String, password: String) : Boolean{
        val lastUser = sharedPref.getString(NAME, null)
        val lastPass = sharedPref.getString(PASSWORD, null)

        val result: Boolean = if (name.isEmpty()){
            Toast.makeText(this, "Please fill name input", Toast.LENGTH_SHORT).show()
            false
        } else if(password.isEmpty()){
            Toast.makeText(this, "Please fill password input", Toast.LENGTH_SHORT).show()
            false
        } else if (lastUser == name) {
            if (lastPass != password){
                Toast.makeText(this, "Password incorrect", Toast.LENGTH_SHORT).show()
                return false
            } else {
                return true
            }
        } else{
            true
        }
        return result
    }

    companion object{
        const val USER_DATA = "user_data"
        const val NAME = "name"
        const val PASSWORD = "password"
        const val IS_LOGIN = "is_login"

        fun open(activity: AppCompatActivity){
            val intent = Intent(activity, LoginActivity::class.java)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}