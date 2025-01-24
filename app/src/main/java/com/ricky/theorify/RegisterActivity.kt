package com.ricky.theorify

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity (){
    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var rp_passwordInput : EditText
    lateinit var registerBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        usernameInput = findViewById(R.id.username_register_input)
        passwordInput = findViewById(R.id.password_register_input)
        rp_passwordInput = findViewById(R.id.password_register_repeat_input)
        registerBtn = findViewById(R.id.register_btn)

        registerBtn.setOnClickListener{
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            Log.i("TestCredentials", "Username : $username and Password : $password")
        }
    }
}