package com.ricky.theorify.model

// Modelo da do JSON que será enviado para a BD no ato de verificação do estado de um instrumento
data class InstrumentState(
    val UtilizadorID : Int,
    val Name : String
)
