package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R

class PauseActivity: AppCompatActivity() {

    // Declaração dos elementos que compõem a página
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

        // Captura dos elementos que compõem a página
        notebutton = findViewById(R.id.note_button)
        pausabutton = findViewById(R.id.pausa_button)
        instrumentobutton = findViewById(R.id.instrumento_button)
        homebutton = findViewById(R.id.home_button)
        clavebutton = findViewById(R.id.clave_button)
        toolbutton = findViewById(R.id.tools_button)
        profilebutton = findViewById(R.id.profile_button)

        // Evento "onClick" do botão de ir para a atividade "NoteActivity"
        notebutton.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "InstrumentActivity"
        instrumentobutton.setOnClickListener{
            val intent = Intent(this, InstrumentActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "MainPageActivity"
        homebutton.setOnClickListener{
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "ClaveActivity"
        clavebutton.setOnClickListener{
            val intent = Intent(this, ClaveActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "ToolsActivity"
        toolbutton.setOnClickListener{
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "ManageActivity"
        profilebutton.setOnClickListener{
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }
    }
}