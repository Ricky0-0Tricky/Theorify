package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tools_page)

        // Captura dos elementos que compõem a página
        voiceDetector = findViewById(R.id.DV_Layout)
        explorerNotes = findViewById(R.id.NE_Layout)
        explorerScales = findViewById(R.id.SE_Layout)
        explorerChords = findViewById(R.id.CE_Layout)
        circleOfFifths = findViewById(R.id.COF_Layout)

        // Evento "onClick" da LinearView de Deteção de Voz
        voiceDetector.setOnClickListener{
            // Criação da Nova Atividade
            val intent = Intent(this, InstrumentActivity::class.java)
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
    }
}