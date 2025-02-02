package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R

class PauseActivity: AppCompatActivity() {

    lateinit var notebutton : ImageButton
    lateinit var pausabutton : ImageButton
    lateinit var instrumentobutton : ImageButton
    lateinit var homebutton : ImageButton
    lateinit var clavebutton : ImageButton
    lateinit var toolbutton : ImageButton
    lateinit var profilebutton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pausa_page)

        notebutton = findViewById(R.id.note_button)
        pausabutton = findViewById(R.id.pausa_button)
        instrumentobutton = findViewById(R.id.instrumento_button)
        homebutton = findViewById(R.id.home_button)
        clavebutton = findViewById(R.id.clave_button)
        toolbutton = findViewById(R.id.tools_button)
        profilebutton = findViewById(R.id.profile_button)

        notebutton.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        instrumentobutton.setOnClickListener{
            val intent = Intent(this, InstrumentActivity::class.java)
            startActivity(intent)
        }

        homebutton.setOnClickListener{
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

        clavebutton.setOnClickListener{
            val intent = Intent(this, ClaveActivity::class.java)
            startActivity(intent)
        }

        toolbutton.setOnClickListener{
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }

        profilebutton.setOnClickListener{
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }
    }
}