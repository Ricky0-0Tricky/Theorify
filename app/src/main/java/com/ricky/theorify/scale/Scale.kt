package theorify.lib.scale

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import theorify.lib.interfaces.Scalable
import theorify.lib.note.Note
import java.io.File
import java.util.Locale

/**
 * Descrição: A seguinte classe representa uma escala musical.
 */
class Scale : Scalable {

    // Atributos Dinâmicos
    private var rootNote: Note? = null
    private var quality: String? = null
    public var scaleNotes: MutableList<Note>? = null

    // Enumeração com todas as qualidades possíveis
    private enum class ScaleQuality {
        MAJOR, MAJOR_PENTATONIC, MAJOR_BLUES_PENTATONIC,
        MINOR, MINOR_PENTATONIC, MINOR_BLUES_PENTATONIC, MELODIC_MINOR, HARMONIC_MINOR, JAZZ_HARMONIC_MINOR, HUNGARY_MINOR,
        WHOLE_TONE,
    }

    /**
     * Construtor por Defeito.
     */
    constructor() {
        this.rootNote = Note("A", 0, "Piano")
        this.quality = ScaleQuality.MAJOR.toString()
        buildScale()
    }

    /**
     * Construtor Parametrizado.
     *
     * @param rootNote Nota raíz da escala
     * @param quality Qualidade da escala
     */
    constructor(rootNote: Note, quality: String) {
        if (isValidQuality(quality)) {
            this.rootNote = rootNote
            this.quality = quality.uppercase(Locale.getDefault())
            this.scaleNotes = ArrayList()
            buildScale()
        }
    }

    /**
     * Metodo para retornar as notas da escala.
     *
     * @return String com as notas da escala
     */
    private fun getScaleNotes(): String {
        if (scaleNotes!!.isEmpty()) {
            return "Não existem notas da escala."
        }
        val notesBuilder = StringBuilder()
        for (scaleNote in scaleNotes!!) {
            notesBuilder.append(scaleNote.noteName).append(scaleNote.octave).append(" ")
        }
        return notesBuilder.toString().trim { it <= ' ' }
    }

    /**
     * Metodo para avaliar a validade do atributo "quality".
     *
     * @param avalQuality Qualidade a avaliar
     * @return Resultado lógico da avaliação
     */
    private fun isValidQuality(avalQuality: String): Boolean {
        for (quality in ScaleQuality.entries) {
            if (quality.name.equals(avalQuality, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Metodo para tocar a escala.
     */
    override fun playScale() {
        if (scaleNotes.isNullOrEmpty()) return
        CoroutineScope(Dispatchers.Main).launch {
            for (note in scaleNotes!!) {
                note.playNote()
                delay(1000)
            }
        }
    }

    /**
     * Metodo para construir a escala segundo a qualidade.
     */
    fun buildScale() {
        // Array com todas as notas existentes
        val notes = arrayOf(
            "A",
            "Asharp",
            "B",
            "C",
            "Dflat",
            "D",
            "Dsharp",
            "E",
            "F",
            "Gflat",
            "G",
            "Gsharp"
        )

        // Array com os semi-tons necessários para construir a escala
        var semiTonesNeeded: IntArray? = null

        // Índice do octave atual
        val octaveIndex = rootNote!!.octave

        // Índice da rootNote no Array de Notas
        val rootIndex = findRootIndex()

        // Adiciona a rootNote à lista de scaleNotes
        scaleNotes!!.add(rootNote!!)

        // Inserção dos semi-tons necessários à construção da escala
        semiTonesNeeded = when (this.quality) {
            "MAJOR" ->
                intArrayOf(2, 4, 5, 7, 9, 11, 12)

            "MAJOR_PENTATONIC" ->
                intArrayOf(2, 4, 7, 9, 12)

            "MAJOR_BLUES_PENTATONIC" ->
                intArrayOf(2, 3, 4, 7, 9, 12)

            "MINOR" ->
                intArrayOf(2, 3, 5, 7, 8, 10, 12)

            "MINOR_PENTATONIC" ->
                intArrayOf(3, 5, 7, 10, 12)

            "MINOR_BLUES_PENTATONIC" ->
                intArrayOf(3, 5, 6, 7, 10, 12)

            "MELODIC_MINOR" ->
                intArrayOf(2, 3, 5, 7, 9, 11, 12)

            "HARMONIC_MINOR" ->
                intArrayOf(2, 3, 5, 7, 8, 11, 12)

            "JAZZ_HARMONIC_MINOR" ->
                intArrayOf(2, 3, 5, 7, 9, 11, 12)

            "HUNGARY_MINOR" ->
                intArrayOf(2, 3, 6, 7, 8, 11, 12)

            "WHOLE_TONE" ->
                intArrayOf(2, 4, 6, 8, 10, 12)

            else -> throw IllegalArgumentException("Qualidade de Escala Desconhecida: " + this.quality)
        }

        // Acumula os semitons para calcular as oitavas
        var currentSemiTone = 0

        var octaveShift: Int

        // Ciclo para construir a escala
        for (semiTone in semiTonesNeeded) {
            // Adiciona o semitom atual ao total
            currentSemiTone = semiTone

            // Caso particular em que a oitava 0 só possui duas notas
            octaveShift = if ((rootNote!!.noteSound.equals(
                    "A0.wav",
                    ignoreCase = true
                ) && currentSemiTone >= 3 || rootNote!!.noteSound.equals(
                    "Asharp0.wav",
                    ignoreCase = true
                ) && semiTone >= 2 || rootNote!!.noteSound.equals(
                    "B0.wav",
                    ignoreCase = true
                ) && currentSemiTone >= 1)
            ) {
                // Incrementa a mudança de oitava para 1 devido ao caso referido anteriormente
                1
            } else {
                // Calcula a mudança de oitava
                currentSemiTone / 12
            }

            // Determina o índice da nota e o seu respetivo nome
            val noteIndex = (rootIndex + semiTone) % notes.size
            val noteName = notes[noteIndex]


            // Cria a nota ajustando à oitava
            scaleNotes!!.add(
                Note(
                    noteName, octaveIndex + octaveShift,
                    rootNote!!.instrument!!
                )
            )
        }
    }

    /**
     * Metodo para encontrar o Index da rootNote.
     *
     * @return Índice da rootNote
     */
    private fun findRootIndex(): Int {
        // Array com todas as notas musicais
        val notes = arrayOf(
            "A",
            "Asharp",
            "B",
            "C",
            "Dflat",
            "D",
            "Dsharp",
            "E",
            "F",
            "Gflat",
            "G",
            "Gsharp"
        )

        // Ciclo for para encontrar o Índice da rootNote
        for (i in notes.indices) {
            if (notes[i] == rootNote!!.noteName) {
                return i
            }
        }
        // Em caso da nota não ter sido encontrada
        throw IllegalArgumentException("Nota raíz inválida!")
    }

    /**
     * Metodo "toString".
     *
     * @return Definição do Objeto
     */
    override fun toString(): String {
        return "Escala " + rootNote!!.noteName + " " + quality + " com as notas: " + getScaleNotes()
    }
}