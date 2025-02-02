package com.ricky.theorify.recorder

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.IOException

class Recorder(private val context: Context) {
    // Declaração dos objetos necessários ao correto funcionamento da gravação
    private var recorder: MediaRecorder? = null

    /**
     * Metodo para a criação do MediaRecorder
     */
    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    /**
     * Metodo para inicio de gravação de áudio
     */
    fun start(outputFile: File) {
        try {
            recorder = createRecorder()

            recorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(outputFile.absolutePath)

                prepare()
                start()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Metodo para terminar a gravação de áudio
     */
    fun stop() {
        try {
            recorder?.apply {
                stop()
                reset()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            recorder = null
        }
    }
}
