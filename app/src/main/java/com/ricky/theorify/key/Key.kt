/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package theorify.lib.key

import theorify.lib.scale.Scale
import theorify.lib.chord.Chord
import theorify.lib.interfaces.Keyable
import theorify.lib.note.Note
import java.util.Locale

/**
 * Descrição: A seguinte classe representa uma chave musical.
 */
class Key : Keyable {
    // Atributos Dinâmicos
    private var rootNote: Note = Note("A",0,"Piano")
    private var quality: String = "MAJOR"
    private var chordsOfKey: MutableList<Chord> = ArrayList()

    /**
     * Construtor por Defeito
     */
    constructor() {
        this.rootNote = Note("A", 0, "Piano")
        this.quality = "MAJOR"
        this.chordsOfKey = ArrayList()
    }

    /**
     * Construtor Parametrizado.
     * @param rootNote Nota raíz da chave
     * @param quality Qualidade da chave
     */
    constructor(rootNote: Note, quality: String) {
        if (isValidQuality(quality.uppercase(Locale.getDefault()))) {
            setRootNote(rootNote)
            setQuality(quality)
            this.chordsOfKey = ArrayList()
            buildKey()
        } else {
            throw IllegalArgumentException("Qualidade Invalida!")
        }
    }

    /**
     * Getter do atributo "rootNote".
     * @return Nota raíz da chave
     */
    override fun getRootNote(): Note {
        return rootNote
    }

    /**
     * Getter do atributo "quality".
     * @return Qualidade da chave
     */
    override fun getQuality(): String {
        return quality
    }

    /**
     * Setter do atributo "rootNote".
     * @param rootNote Nota raíz da chave
     */
    private fun setRootNote(rootNote: Note) {
        this.rootNote = rootNote
    }

    /**
     * Setter do atributo "quality".
     * @param quality Qualidade da chave
     */
    private fun setQuality(quality: String) {
        this.quality = quality
    }

    /**
     * Metodo para avaliar a validade do atributo "quality".
     * @param avalQuality Qualidade a avaliar
     * @return Resultado lógico da avaliação
     */
    private fun isValidQuality(avalQuality: String): Boolean {
        if (avalQuality.equals("MAJOR", ignoreCase = true) || avalQuality.equals("MINOR", ignoreCase = true)) {
            return true
        }
        return false
    }

    /**
     * Metodo para construir a chave musical segundo a qualidade.
     */
    private fun buildKey() {
        when (this.quality) {
            "Major" -> {
                val majorScale = Scale(rootNote, "Major")
                val majorQualities =
                    arrayOf("Major", "Minor", "Minor", "Major", "Major", "Minor", "Diminished")
                var i = 0
                while (i < 7) {
                    chordsOfKey.add(i, Chord(majorScale.scaleNotes!!.get(i), majorQualities[i]))
                    i++
                }
            }

            "Minor" -> {
                val minorScale = Scale(rootNote, "Minor")
                val minorQualities =
                    arrayOf("Minor", "Diminished", "Major", "Minor", "Minor", "Major", "Major")
                var i = 0
                while (i < 7) {
                    chordsOfKey.add(i, Chord(minorScale.scaleNotes!!.get(i), minorQualities[i]))
                    i++
                }
            }
        }
    }

    /**
     * Metodo para retornar os acordes da chave.
     * @return String com os acordes diatónicos da chave
     */
    private fun getChordsOfKey(): String {
        if (chordsOfKey.isEmpty()) {
            return "Não existem acordes da chave"
        }
        val chordsBuilder = StringBuilder()
        for (chordNote in chordsOfKey) {
            chordsBuilder.append(chordNote.rootNote!!.noteName).append(" ")
                .append(chordNote.quality!!).append(" ")
        }
        return chordsBuilder.toString().trim { it <= ' ' }
    }


    /**
     * Metodo para tocar os acordes diatónicos da chave.
     */
    override fun playChordsOfKey() {
        for (chord in chordsOfKey) {
            chord.playChord()
        }
    }

    /**
     * Metodo "toString".
     * @return Definição do Objeto
     */
    override fun toString(): String {
        return ("Chave: " + rootNote.noteName) + " " + this.quality + " com os acordes: " + getChordsOfKey()
    }
}