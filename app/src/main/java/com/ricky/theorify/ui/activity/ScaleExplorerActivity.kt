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
import theorify.lib.note.Note
import theorify.lib.scale.Scale

class ScaleExplorerActivity: AppCompatActivity() {
    // Declaração dos elementos que compõem a página
    lateinit var pitchSpin : Spinner
    lateinit var octaveSpin : Spinner
    lateinit var scaleSpin : Spinner
    lateinit var playscaleBtn : Button
    lateinit var selectedScaleText : TextView
    lateinit var backbutton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scale_explorer_page)

        // Captura dos elementos que compõem a página
        pitchSpin = findViewById(R.id.pitchSpin)
        octaveSpin = findViewById(R.id.octaveSpin)
        scaleSpin = findViewById(R.id.qualitySpin)
        playscaleBtn = findViewById(R.id.playScaleBtn)
        selectedScaleText = findViewById(R.id.selectedScaleText)
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
        val qualities = arrayOf("Major", "Major_Pentatonic", "Major_Blues_Pentatonic", "Minor", "Minor_Pentatonic", "Minor_Blues_Pentatonic", "Melodic_Minor", "Harmonic_Minor", "Jazz_Harmonic_Minor", "Hungary_Minor", "Whole_Tone")
        scaleSpin.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, qualities)

        // Evento "onClick" do botão de tocar nota
        playscaleBtn.setOnClickListener {
            try {
                if (checkInternetAccess()) {
                    // Captura dos valores escolhidos
                    val selectedPitch = pitchSpin.selectedItem.toString()
                    val selectedOctave = octaveSpin.selectedItem.toString()
                    val selectedQuality =  scaleSpin.selectedItem.toString()
                    // Toca escala
                    var scale = Scale(Note(selectedPitch,selectedOctave.toInt(),PickedInstrument.currentInstrument),selectedQuality)
                    scale.playScale()
                    // Informa o Utilizador da nota selecionada
                    selectedScaleText.setText("A escala tocada foi " + selectedPitch + selectedOctave + " " + selectedQuality + ".")
                } else {
                    exit()
                }
            } catch (ex : Exception) {
                Toast.makeText(this, "Ocorreu um Erro!", Toast.LENGTH_LONG).show()
            }
        }

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