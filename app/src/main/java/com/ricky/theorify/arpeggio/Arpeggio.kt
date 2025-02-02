package theorify.lib.arpeggio

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import theorify.lib.chord.Chord
import theorify.lib.note.Note

/**
 * Descrição: A seguinte classe representa um arpeggio.
 */
class Arpeggio : Chord {
    // Atributos Dinâmicos
    private var arpeggioNotes: MutableList<Note>? = null

    /**
     * Construtor por Defeito.
     */
    constructor() : super() {
        buildArpeggio()
    }


    /**
     * Construtor Parametrizado.
     * @param rootNote Nota raíz
     * @param quality Qualidade do Arpeggio
     */
    constructor(rootNote: Note, quality: String) : super(rootNote, quality) {
        buildArpeggio()
    }

    /**
     * Metodo para construir o arpeggio segundo a qualidade.
     */
    private fun buildArpeggio() {
        this.arpeggioNotes = super.chordNotes
    }

    /**
     * Metodo para tocar o arpeggio.
     */
    fun playArpeggio() {
        if (arpeggioNotes.isNullOrEmpty()) return
        CoroutineScope(Dispatchers.Main).launch {
            for (note in arpeggioNotes!!) {
                note.playNote()
                delay(1500)
            }
        }
    }

    /**
     * Getter do parâmetro "arpeggioNotes".
     * @return String com as notas do arpeggio
     */
    private fun getArpeggioNotes(): String {
        // Verifica se existem notas do arpeggio
        if (arpeggioNotes!!.isEmpty()) {
            return "Não existem notas do arpeggio."
        }
        val notesBuilder = StringBuilder()
        for (arpeggioNote in arpeggioNotes!!) {
            notesBuilder.append(arpeggioNote.noteName).append(arpeggioNote.octave)
                .append(" ")
        }
        return notesBuilder.toString().trim { it <= ' ' }
    }

    /**
     * Metodo "toString".
     * @return Definição do Objeto
     */
    override fun toString(): String {
        return ("Arpeggio: " + super.rootNote!!.noteName + " " + super.quality + " com as notas " + getArpeggioNotes())
    }
}