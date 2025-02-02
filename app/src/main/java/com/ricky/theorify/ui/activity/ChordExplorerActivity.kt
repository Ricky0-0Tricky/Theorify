package com.ricky.theorify.ui.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R
import com.ricky.theorify.model.PickedInstrument
import com.ricky.theorify.model.RegisteredUser
import theorify.lib.arpeggio.Arpeggio
import theorify.lib.chord.Chord
import theorify.lib.note.Note

class ChordExplorerActivity: AppCompatActivity() {
    // Declaração dos elementos que compõem a página
    lateinit var pitchSpin : Spinner
    lateinit var octaveSpin : Spinner
    lateinit var chordSpin : Spinner
    lateinit var playchordBtn : Button
    lateinit var playarpeggioBtn : Button
    lateinit var selectedChordText : TextView
    private var isRunning : Boolean = false
    lateinit var backbutton : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chord_explorer_page)

        // Captura dos elementos que compõem a página
        pitchSpin = findViewById(R.id.pitchSpin)
        octaveSpin = findViewById(R.id.octaveSpin)
        chordSpin = findViewById(R.id.qualitySpin)
        playchordBtn = findViewById(R.id.playChordBtn)
        playarpeggioBtn = findViewById(R.id.playArpeggioBtn)
        selectedChordText = findViewById(R.id.selectedChordText)
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
                val octaves = (2..5).map { it.toString() }
                octaveSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, octaves)
            }
        }

        // Preenchimento do Spinner de qualidades
        val qualities = arrayOf("Major", "Major_6", "Dominant_7", "Major_7", "Dominant_9", "Major_9", "Major_11", "Major_13",
            "Minor", "Minor_6", "Minor_Major", "Minor_7", "Minor_9", "Minor_11", "Minor_13", "Sus_2", "Sus_4", "Diminished", "Diminished_7", "Augmented",
            "Dominant_7_Flat_9", "Dominant_7_Sharp_9", "Dominant_7_Flat_5", "Dominant_7_Sharp_5", "Add_9", "Half_Diminished", "Power")
        chordSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qualities)

        // Evento "onClick" do botão de tocar acorde
        playchordBtn.setOnClickListener {
            try {
                if (checkInternetAccess()) {
                    // Captura dos valores escolhidos
                    val selectedPitch = pitchSpin.selectedItem.toString()
                    val selectedOctave = octaveSpin.selectedItem.toString()
                    val selectedQuality =  chordSpin.selectedItem.toString()
                    // Toca acorde
                    var chord = Chord(Note(selectedPitch,selectedOctave.toInt(), PickedInstrument.currentInstrument),selectedQuality)
                    chord.playChord()
                    // Informa o Utilizador da nota selecionada
                    selectedChordText.setText("O acorde tocado foi " + selectedPitch + selectedOctave + " " + selectedQuality + ".")
                } else {
                    exit()
                }
            } catch (ex : Exception) {
                ex.printStackTrace()
                Toast.makeText(this, "Acorde não suportado pelo instrumento!", Toast.LENGTH_LONG).show()
            }
        }

        playarpeggioBtn.setOnClickListener{
            try {
                if (checkInternetAccess()) {
                    // Captura dos valores escolhidos
                    val selectedPitch = pitchSpin.selectedItem.toString()
                    val selectedOctave = octaveSpin.selectedItem.toString()
                    val selectedQuality =  chordSpin.selectedItem.toString()
                    // Toca arpeggio
                    var arp = Arpeggio(Note(selectedPitch,selectedOctave.toInt(),PickedInstrument.currentInstrument), selectedQuality)
                    arp.playArpeggio()
                    // Informa o Utilizador da nota selecionada
                    selectedChordText.setText("O arpeggio tocado foi " + selectedPitch + selectedOctave + " " + selectedQuality + ".")
                } else {
                    exit()
                }
            } catch (ex : Exception) {
                Toast.makeText(this, "Arpeggio não suportado pelo instrumento!", Toast.LENGTH_LONG).show()
            }
        }

        // Evento "onClick" do botão de voltar atividade "ToolsActivity"
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
        Toast.makeText(this,"Não existe conectividade...A redireciona-lo para a página de login",
            Toast.LENGTH_LONG).show()
        // Redirecionamento para a página de login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}