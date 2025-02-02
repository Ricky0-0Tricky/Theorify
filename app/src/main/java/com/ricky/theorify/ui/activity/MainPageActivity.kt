package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R
import com.ricky.theorify.model.InstrumentState

class MainPageActivity : AppCompatActivity(){

    lateinit var aboutImage : ImageButton
    lateinit var notebutton : ImageButton
    lateinit var pausabutton : ImageButton
    lateinit var instrumentobutton : ImageButton
    lateinit var homebutton : ImageButton
    lateinit var clavebutton : ImageButton
    lateinit var toolbutton : ImageButton
    lateinit var profilebutton : ImageButton

    val msg : String = "Este App foi desenvolvida por:\nJoão Joaquim nº24729\nRicardo Gonçalves nº24873"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)

        aboutImage = findViewById(R.id.aboutImage)
        notebutton = findViewById(R.id.note_button)
        pausabutton = findViewById(R.id.pausa_button)
        instrumentobutton = findViewById(R.id.instrumento_button)
        homebutton = findViewById(R.id.home_button)
        clavebutton = findViewById(R.id.clave_button)
        toolbutton = findViewById(R.id.tools_button)
        profilebutton = findViewById(R.id.profile_button)


        aboutImage.setOnClickListener{
            buildAlert(msg)
        }

        notebutton.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        pausabutton.setOnClickListener{
            val intent = Intent(this, PauseActivity::class.java)
            startActivity(intent)
        }

        instrumentobutton.setOnClickListener{
            val intent = Intent(this, InstrumentActivity::class.java)
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

        // Evento "onBackPressedDispatcher" do telemóvel
        onBackPressedDispatcher.addCallback(this){
        }
    }

    /**
     * Metodo para construir o Alert Dialog
     */
    fun buildAlert(msg : String){
        // Criação de um AlertDialog com a questão
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Autores")
        builder.setMessage(msg)
        // Criação dos botões do AlertDialog
        builder.setPositiveButton("Ok") { dialog, _ ->
            // Fecha o AlertDialog
            dialog.dismiss()
        }
        // Mostra o AlertDialog
        builder.show()
    }
}