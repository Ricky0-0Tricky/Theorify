package theorify.lib.chord

import android.provider.ContactsContract.CommonDataKinds
import theorify.lib.interfaces.Chordable
import theorify.lib.note.Note
import java.util.Locale

/**
 * Descrição: A seguinte classe representa um acorde.
 */
open class Chord : Chordable {
    // Atributos Dinâmicos
    var rootNote: Note? = null
    var quality: String? = null
    protected var chordNotes: MutableList<Note>? = null

    // Enumeração com todas as qualidades possíveis
    private enum class ChordQuality {
        MAJOR, MAJOR_6, DOMINANT_7, MAJOR_7, DOMINANT_9, MAJOR_9, MAJOR_11, MAJOR_13,
        MINOR, MINOR_6, MINOR_MAJOR, MINOR_7, MINOR_9, MINOR_11, MINOR_13,
        SUS_2, SUS_4,
        DIMINISHED, DIMINISHED_7, AUGMENTED,
        DOMINANT_7_FLAT_9, DOMINANT_7_SHARP_9, DOMINANT_7_FLAT_5, DOMINANT_7_SHARP_5,
        ADD_9, HALF_DIMINISHED,
        POWER
    }

    /**
     * Construtor por Defeito.
     */
    constructor() {
        this.rootNote = Note("A", 0, "Piano")
        this.quality = ChordQuality.MAJOR.toString()
        this.chordNotes = ArrayList()
        buildChord()
    }

    /**
     * Construtor Parametrizado.
     * @param rootNote Nota raíz
     * @param quality Qualidade do acorde
     */
    constructor(rootNote: Note, quality: String) {
        if (isValidQuality(quality)) {
            this.rootNote = rootNote
            this.quality = quality.uppercase(Locale.getDefault())
            this.chordNotes = ArrayList()
            buildChord()
        } else {
            throw IllegalArgumentException("Qualidade Inválida!")
        }
    }

    /**
     * Metodo para avaliar a validade do atributo "quality".
     * @param avalQuality Qualidade a avaliar
     * @return Resultado lógico da avaliação
     */
    private fun isValidQuality(avalQuality: String): Boolean {
        for (quality in ChordQuality.entries) {
            if (quality.name.equals(avalQuality, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    /**
     * Metodo para construir o acorde segundo a qualidade.
     */
    private fun buildChord() {
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

        // Array com os semi-tons necessários para construir o acorde
        var semiTonesNeeded: IntArray? = null

        // Índice do octave atual
        val octaveIndex: Int = rootNote!!.octave

        // Índice da rootNote no Array de Notas
        val rootIndex = findRootIndex()

        // Adiciona a rootNote à lista de chordNotes
        chordNotes!!.add(rootNote!!)

        // Inserção dos semi-tons necessários à construção do Acorde
        when (this.quality) {
            "MAJOR" ->
                semiTonesNeeded = intArrayOf(4, 7)

            "MAJOR_6" ->
                semiTonesNeeded = intArrayOf(4, 7, 9)

            "DOMINANT_7" ->
                semiTonesNeeded = intArrayOf(4, 7, 10)

            "MAJOR_7" ->
                semiTonesNeeded = intArrayOf(4, 7, 11)

            "DOMINANT_9" ->
                semiTonesNeeded = intArrayOf(4, 7, 10, 14)

            "MAJOR_9" ->
                semiTonesNeeded = intArrayOf(4, 7, 11, 14)

            "MAJOR_11" ->
                semiTonesNeeded = intArrayOf(4, 7, 11, 14, 17)

            "MAJOR_13" ->
                semiTonesNeeded = intArrayOf(4, 7, 11, 14, 17, 21)

            "MINOR" ->
                semiTonesNeeded = intArrayOf(3, 7)

            "MINOR_6" ->
                semiTonesNeeded = intArrayOf(3, 7, 9)

            "MINOR_MAJOR" ->
                semiTonesNeeded = intArrayOf(3, 7, 11)

            "MINOR_7" ->
                semiTonesNeeded = intArrayOf(3, 7, 10)

            "MINOR_9" ->
                semiTonesNeeded = intArrayOf(3, 7, 10, 14)

            "MINOR_11" ->
                semiTonesNeeded = intArrayOf(3, 7, 10, 14, 17)

            "MINOR_13" ->
                semiTonesNeeded = intArrayOf(3, 7, 10, 14, 17, 21)

            "SUS_2" ->
                semiTonesNeeded = intArrayOf(2, 7)

            "SUS_4" ->
                semiTonesNeeded = intArrayOf(5, 7)

            "DIMINISHED" ->
                semiTonesNeeded = intArrayOf(3, 6)

            "DIMINISHED_7" ->
                semiTonesNeeded = intArrayOf(3, 6, 10)

            "AUGMENTED" ->
                semiTonesNeeded = intArrayOf(4, 8)

            "DOMINANT_7_FLAT_9" ->
                semiTonesNeeded = intArrayOf(4, 7, 10, 13)

            "DOMINANT_7_SHARP_9" ->
                semiTonesNeeded = intArrayOf(4, 7, 10, 15)

            "DOMINANT_7_FLAT_5" ->
                semiTonesNeeded = intArrayOf(4, 6, 10)

            "DOMINANT_7_SHARP_5" ->
                semiTonesNeeded = intArrayOf(4, 8, 10)

            "ADD_9" ->
                semiTonesNeeded = intArrayOf(4, 7, 14)

            "HALF_DIMINISHED" ->
                semiTonesNeeded = intArrayOf(3, 6, 10)

            "POWER" ->
                semiTonesNeeded = intArrayOf(6, 12)

            else -> {}
        }

        // Acumula os semitons para calcular as oitavas
        var currentSemiTone = 0

        var octaveShift: Int

        // Ciclo para construir o acorde
        for (semiTone in semiTonesNeeded!!) {
            // Adiciona o semitom atual ao total
            currentSemiTone = semiTone

            // Caso particular em que a oitava 0 só possui duas notas
            octaveShift = if ((rootNote!!.noteSound.equals("A0.wav") && currentSemiTone >= 3 || rootNote!!.noteSound.equals("Asharp0.wav") && semiTone >= 2 || rootNote!!.noteSound.equals("B0.wav") && currentSemiTone >= 1)) {
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
            chordNotes!!.add(Note(noteName, octaveIndex + octaveShift, rootNote!!.instrument))
        }
    }

    /**
     * Metodo para tocar o acorde.
     */
    override fun playChord() {
        // Lista que irá conter as Threads
        val threads: MutableList<Thread> = ArrayList()

        for (note in chordNotes!!) {
            // Criação de uma Thread para cada nota
            val noteThread = Thread {
                note.playNote()
            }

            // Inicia as threads
            noteThread.start()

            // Adiciona a Thread à lista
            threads.add(noteThread)
        }

        // Espera que todas as Threads terminem a sua nota
        for (thread in threads) {
            try {
                thread.join()
            } catch (e: InterruptedException) {
                println("Erro ao esperar que a nota terminasse: " + e.message)
            }
        }
    }

    /**
     * Metodo para encontrar o Index da rootNote.
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
     * Metodo para retornar as notas do acorde.
     * @return String com as notas do acorde
     */
    private fun getChordNotes(): String {
        if (chordNotes!!.isEmpty()) {
            return "Não existem notas do acorde."
        }
        val notesBuilder = StringBuilder()
        for (chordNote in chordNotes!!) {
            notesBuilder.append(chordNote.noteName).append(chordNote.octave).append(" ")
        }
        return notesBuilder.toString().trim { it <= ' ' }
    }

    /**
     * Metodo "toString".
     * @return Definição do Objeto
     */
    override fun toString(): String {
        return ("Acorde: " + rootNote!!.noteName).toString() + " " + quality + " com as notas: " + getChordNotes()
    }
}