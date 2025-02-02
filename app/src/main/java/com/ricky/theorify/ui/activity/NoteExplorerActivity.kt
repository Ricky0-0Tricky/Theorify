package com.ricky.theorify.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R
import com.ricky.theorify.model.PickedInstrument
import com.ricky.theorify.model.RegisteredUser
import theorify.lib.note.Note

class NoteExplorerActivity: AppCompatActivity() {
    // Declaração dos elementos que compõem a página
    lateinit var pitchSpin : Spinner
    lateinit var octaveSpin : Spinner
    lateinit var playnoteBtn : Button
    lateinit var selectedNoteText : TextView
    lateinit var backbutton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_explorer_page)

        // Captura dos elementos que compõem a página
        pitchSpin = findViewById(R.id.pitchSpin)
        octaveSpin = findViewById(R.id.octaveSpin)
        playnoteBtn = findViewById(R.id.playNoteBtn)
        selectedNoteText = findViewById(R.id.selectedNoteText)
        backbutton = findViewById(R.id.back_arrowImage)

        // Preenchimento do Spinner de notas
        val notes = arrayOf("C", "Dflat", "D", "Dsharp", "E", "F", "Gflat", "G", "Gsharp", "A", "Asharp", "B")
        pitchSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, notes)

        // Preenchimento do Spinner de oitavas tendo em conta o instrumento
        when (PickedInstrument.currentInstrument) {
            "Piano" -> {
                val octaves = (0..8).map { it.toString() }
                octaveSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, octaves)
            }
            else -> {
                val octaves = (2..6).map { it.toString() }
                octaveSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, octaves)
            }
        }

        // Evento "onClick" do botão de tocar nota
        playnoteBtn.setOnClickListener {
            try {
                if (checkInternetAccess()) {
                    // Captura dos valores escolhidos
                    val selectedPitch = pitchSpin.selectedItem.toString()
                    val selectedOctave = octaveSpin.selectedItem.toString()
                    // Toca nota
                    var note = Note(selectedPitch, selectedOctave.toInt(), PickedInstrument.currentInstrument)
                    note.playNote()
                    // Informa o Utilizador da nota selecionada
                    selectedNoteText.setText("A nota tocada foi um " + selectedPitch + selectedOctave + ".")
                } else {
                    exit()
                }
                } catch (ex : Exception) {
                    Toast.makeText(this, "Nota não suportada pelo instrumento!", Toast.LENGTH_LONG).show()
                }
        }

        // Evento "onClick" do botão de voltar à atividade "ToolsActivity"
        backbutton.setOnClickListener{
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }
    }

        /**
         * Metodo para verificar a conectividade do Utilizador
         */
        fun checkInternetAccess() : Boolean {
            val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            }
            return false
        }

        /**
         * Metodo para voltar ao Login caso não exista conectividade
         **/
        fun exit() {
            // Reposição do valor por defeito do instrumento e utilizador
            PickedInstrument.currentInstrument = "Piano"
            RegisteredUser.currentUser = null
            // Aviso ao Utilizador do redirecionamento
            Toast.makeText(this,"Não existe conectividade...A redireciona-lo para a página de login",Toast.LENGTH_LONG).show()
            // Redirecionamento para a página de login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }