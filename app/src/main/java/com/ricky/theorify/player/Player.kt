package com.ricky.theorify.player

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class Player(private val context: Context) {
    // Declaração dos objetos necessários ao correto funcionamento do playback
    private var audioTrack: AudioTrack? = null

    /**
     * Toca o ficheiro com o pitch aplicado
     */
    fun playFileWithPitch(file: File, pitchFactor: Float) {
        // Obtém o sample rate nativo do dispositivo
        val nativeSampleRate = AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_MUSIC)

        // Aplica o pitch sobre o sample rate
        val adjustedSampleRate = (nativeSampleRate * pitchFactor).toInt()

        // Cálculo do buffer size mínimo suportado pelo dispositivo
        val adjustedBufferSize = AudioTrack.getMinBufferSize(
            adjustedSampleRate,
            AudioFormat.CHANNEL_OUT_STEREO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        // Cria uma AudioTrack para playback
        audioTrack = AudioTrack.Builder()
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(adjustedSampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                    .build()
            )
            .setBufferSizeInBytes(adjustedBufferSize)
            .build()

        // Começa a tocar o ficheiro
        playAudioFile(file, adjustedBufferSize)
    }

    /**
     * Metodo para ler os ficheiro em chuncks e alimentá-lo ao AudioTrack
     */
    private fun playAudioFile(file: File, bufferSize: Int) {
        // Abre o ficheiro como uma input stream
        try {
            val inputStream = FileInputStream(file)
            val buffer = ByteArray(bufferSize)
            var bytesRead: Int

            audioTrack?.play()

            // Lê o ficheiro em bocados e passa os para AudioTrack
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                audioTrack?.write(buffer, 0, bytesRead)
            }

            // Para o playback quando o ficheiro estiver terminado
            stop()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Metodo para parar o playback
     */
    fun stop() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}
