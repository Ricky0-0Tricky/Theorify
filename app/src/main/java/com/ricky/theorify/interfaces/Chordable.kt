package theorify.lib.interfaces

import theorify.lib.note.Note

/**
 * Descrição: A seguinte interface representa os comportamentos esperados de um acorde musical.
 */
interface Chordable {
    /**
     * Toca o acorde musical.
     */
    fun playChord()
}