package com.ricky.theorify.ui.activity

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import com.ricky.theorify.R
import com.ricky.theorify.recorder.Recorder
import com.ricky.theorify.player.Player
import java.io.File

class DetectionNoteActivity : AppCompatActivity() {
    // Declaração dos elementos que compõem a página
    lateinit var micBtn: ImageButton
    lateinit var pitch: TextView
    private var isRecording = false
    private var isPlaying = false

    // Declaração dos objetos necessários ao correto funcionamento da funcionalidade
    private val recorder: Recorder by lazy { Recorder(this) }
    private val player: Player by lazy { Player(this) }
    private lateinit var outputFile: File
    lateinit var backbutton : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_detection_page)

        // Captura dos elementos que compõem a página
        micBtn = findViewById(R.id.buttonMic)
        pitch = findViewById(R.id.pitch)
        backbutton = findViewById(R.id.back_arrowImage)

        // Declaração do Ficheiro onde será guardada a gravação
        outputFile = File(filesDir, "recordedAudio.wav")

        // Evento "onClick" do botão de microfone
        micBtn.setOnClickListener {
            checkAudioPermission()
        }

        // Evento "onClick" do botão de voltar à atividade "ToolsActivity"
        backbutton.setOnClickListener{
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Para o gravador e player
     */
    override fun onDestroy() {
        super.onDestroy()
        if (isRecording) {
            recorder.stop()
        }
        if (isPlaying) {
            player.stop()
        }
    }

    /**
     * Verifica a permissão de gravação de áudio
     */
    private fun checkAudioPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED -> {
                handleRecordingOrPlayback()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                Toast.makeText(this, "Permissões de Áudio são necessárias para a gravação de áudio!", Toast.LENGTH_LONG).show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    /**
     * Lida com gravação e playback de áudio
     */
    private fun handleRecordingOrPlayback() {
        if (isRecording) {
            // Para a gravação
            recorder.stop()
            isRecording = false
            pitch.setText("Ouvir")

            // Após parar a gravação, dá-se 500 ms para o mesmo ser guardado com sucesso
            Handler(mainLooper).postDelayed({
                startPlayback()
            }, 500)
        } else if (isPlaying) {
            // Para o playback
            player.stop()
            isPlaying = false
            pitch.setText("Ouvir")
        } else {
            // Começa a gravação
            recorder.start(outputFile)
            isRecording = true
            pitch.setText("Parar")
        }
    }

    /**
     * Metodo para começar o playback
     */
    private fun startPlayback() {
        player.playFileWithPitch(outputFile, 1F)
        isPlaying = true
        pitch.setText("Parar")
    }

    /**
     * Metodo para pedir permissões de gravação de áudio
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                handleRecordingOrPlayback()
            } else {
                Toast.makeText(this, "Permissão Negada...", Toast.LENGTH_LONG).show()
            }
        }
}
