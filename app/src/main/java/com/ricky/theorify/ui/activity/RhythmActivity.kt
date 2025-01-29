package com.ricky.theorify.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R
import com.ricky.theorify.model.RegisteredUser

class RhythmActivity : AppCompatActivity(){
    // Declaração dos elementos que compõem a página
    lateinit var firstIcon : ImageView
    lateinit var firstDescription : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rhythm_page)

        // Captura dos elementos que compõem a página
        firstIcon = findViewById(R.id.firstIcon)
        firstDescription = findViewById(R.id.firstDescription)
        //val currentUser = RegisteredUser.currentUser
        //currentUser?.let { Toast.makeText(this, it.ID, Toast.LENGTH_LONG).show() }

    }
}