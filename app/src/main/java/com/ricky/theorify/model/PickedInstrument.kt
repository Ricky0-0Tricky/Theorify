package com.ricky.theorify.model

// Classe representativa da escolha do instrumento
data class PickedInstrument(
    var Name : String
) {
    // Objeto que guardará o utilizador se o mesmo existir e as credênciais corresponderem
    companion object {
        var currentInstrument : String = "Piano"
    }
}
