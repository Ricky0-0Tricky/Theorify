package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R

class ToolsActivity: AppCompatActivity(){
    // Declaração dos elementos que compõem a página
    lateinit var voiceDetector : LinearLayout
    lateinit var explorerNotes : LinearLayout
    lateinit var explorerScales : LinearLayout
    lateinit var explorerChords : LinearLayout
    lateinit var circleOfFifths : LinearLayout
    lateinit var notebutton : ImageButton
    lateinit var pausabutton : ImageButton
    lateinit var instrumentobutton : ImageButton
    lateinit var homebutton : ImageButton
    lateinit var clavebutton : ImageButton
    lateinit var toolbutton : ImageButton
    lateinit var profilebutton : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tools_page)

        // Captura dos elementos que compõem a página
        voiceDetector = findViewById(R.id.DV_Layout)
        explorerNotes = findViewById(R.id.NE_Layout)
        explorerScales = findViewById(R.id.SE_Layout)
        explorerChords = findViewById(R.id.CE_Layout)
        circleOfFifths = findViewById(R.id.COF_Layout)
        notebutton = findViewById(R.id.note_button)
        pausabutton = findViewById(R.id.pausa_button)
        instrumentobutton = findViewById(R.id.instrumento_button)
        homebutton = findViewById(R.id.home_button)
        clavebutton = findViewById(R.id.clave_button)
        toolbutton = findViewById(R.id.tools_button)
        profilebutton = findViewById(R.id.profile_button)


        // Evento "onClick" da LinearView de Deteção de Voz
        voiceDetector.setOnClickListener{
            // Criação da Nova Atividade
            val intent = Intent(this, DetectionNoteActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" da LinearView do Explorador de Notas
        explorerNotes.setOnClickListener{
            // Criação da Nova Atividade
            val intent = Intent(this, NoteExplorerActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" da LinearView do Explorador de Escalas
        explorerScales.setOnClickListener{
            // Criação da Nova Atividade
            val intent = Intent(this, ScaleExplorerActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" da LinearView do Explorador de Acordes
        explorerChords.setOnClickListener{
            // Criação da Nova Atividade
            val intent = Intent(this, ChordExplorerActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" da LinearView do Círculo de Quintas
        circleOfFifths.setOnClickListener{
            // Criação da Nova Atividade
            val intent = Intent(this, CircleofFifthsActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "NoteActivity"
        notebutton.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        // Evento "onClick" do botão de ir para a atividade "PauseActivity"
        pausabutton.setOnClickListener{
            val intent = Intent(this, PauseActivity::class.java)
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

        // Evento "onClick" do botão de ir para a atividade "ManageActivity"
        profilebutton.setOnClickListener{
            val intent = Intent(this, ManageActivity::class.java)
            startActivity(intent)
        }

    }

}