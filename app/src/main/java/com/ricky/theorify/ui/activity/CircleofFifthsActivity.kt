package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R

class CircleofFifthsActivity: AppCompatActivity() {

    lateinit var backbutton : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.circle_of_fifths_page)

        backbutton = findViewById(R.id.back_arrowImage)

        backbutton.setOnClickListener{
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }
    }
}