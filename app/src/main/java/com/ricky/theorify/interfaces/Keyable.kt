package theorify.lib.interfaces

import android.provider.ContactsContract.CommonDataKinds
import theorify.lib.note.Note

/**
 * Descrição: A seguinte interface representa os comportamentos esperados de uma chave musical.
 */
interface Keyable {
    /**
     * Retorna a nota raíz da chave musical.
     * @return nota raíz da chave
     */
    fun getRootNote() : Note

    /**
     * Retorna a qualidade da chave musical.
     * @return qualidade da chave
     */
    fun getQuality() : String

    /**
     * Toca os acordes diatónicos da chave musical.
     */
    fun playChordsOfKey()
}