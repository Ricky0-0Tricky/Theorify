package theorify.lib.note

import android.media.MediaPlayer
import theorify.lib.interfaces.Noteable
import java.io.File

/**
 * Descrição: A seguinte classe representa uma única nota musical.
 */
public class Note : Noteable {
    // Atributos Dinâmicos
    public var noteName: String = "A"
    public var noteSound: String = "A0.wav"
    public var octave: Int = 0
    public var instrument: String = "Piano"


    // Enumeração com todas as notas possíveis
    private enum class Notes {
        A, Asharp,
        Bflat, B,
        C,
        Dflat, D, Dsharp,
        E,
        F,
        Gflat, G, Gsharp
    }

    /**
     * Construtor por Defeito.
     */
    constructor() {
        this.noteName = "A"
        this.octave = 0
        this.noteSound = "A" + this.octave + ".wav"
        this.instrument = "Piano"
    }

    /**
     * Construtor Parametrizado.
     * @param noteName Nome da nota musical
     * @param octave Oitava da nota musical
     * @param instrument Instrumento escolhido
     */
    constructor(noteName: String, octave: Int, instrument: String) {
        if (isValidNote(noteName) && isValidInstrument(instrument) && isValidOctave(octave, instrument) && isValidNoteSound(noteName, octave, instrument)) {
            this.noteName = noteName
            this.octave = octave
            this.noteSound = noteName + this.octave + ".wav"
            this.instrument = instrument
        } else {
            throw IllegalArgumentException("Nome da nota ou oitava invalidos ou instrumento invalido")
        }
    }

    /**
     * Metodo para avaliar a validade do atributo "noteName".
     * @return Avaliação do noteName
     */
    private fun isValidNote(noteName: String): Boolean {
        for (note in Notes.entries) {
            if (note.toString().equals(noteName, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Metodo para avaliar a validade do atributo "octave".
     * @return Avaliação do octave
     */
    private fun isValidOctave(octave: Int, instrument: String): Boolean {
        return when (instrument) {
            "Piano" -> {
                if (octave >= 0 && octave <= 7) {
                    true
                } else {
                    false
                }
            }
            else -> {
                if (octave >= 2 && octave <= 6) {
                    true
                } else {
                    false
                }
            }
        }
    }

    /**
     * Metodo para avaliar a validade do futuro atributo "noteSound".
     * @return Avalição da existência da nota nos ficheiros
     */
    private fun isValidNoteSound(noteName: String, octave: Int, instrument: String): Boolean {
        try {
            // Obtém o diretório de arquivos internos do aplicativo
            val baseDir = "/data/data/com.ricky.theorify/files"

            // Constrói o caminho total
            val filePath = "$baseDir/$instrument/$noteName$octave.wav"

            // Verifica se o arquivo existe
            val audioFile = File(filePath)
            // Existe
            return audioFile.exists()
        } catch (ex: Exception) {
            // Não existe
            return false
        }
    }

    /**
     * Metodo para avaliar a validade do atributo "instrument".
     * @return Avaliação do instrument
     */
    private fun isValidInstrument(instrument: String): Boolean {
        if (instrument == "Piano" || instrument == "Acoustic_Guitar" || instrument == "Electric_Guitar") {
            return true
        }
        return false
    }

    /**
     * Metodo que toca a nota musical.
     */
    override public fun playNote() {
        try {
            val filePath = "/data/data/com.ricky.theorify/files/$instrument/$noteSound"
            // Carrega o ficheiro de áudio da nota
            val file = File(filePath)
            if (file.exists()) {
                try {
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(filePath)
                    mediaPlayer.prepare()
                    mediaPlayer.start()

                    mediaPlayer.setOnCompletionListener {
                        mediaPlayer.release()
                    }
                } catch (ex: Exception) {
                    println("Erro ao tocar a nota: ${ex.message}")
                }
            }
        } catch(ex: Exception){
            println("Erro ao tocar a nota: ${ex.message}")
        }
    }

    /**
     * Metodo "toString".
     * @return Definição do Objeto
     */
    override fun toString(): String {
        return "Nota Musical: " + this.noteName + " na oitava " + this.octave
    }
}