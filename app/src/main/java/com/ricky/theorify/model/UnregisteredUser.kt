package com.ricky.theorify.model

// Modelo do JSON que será enviado para a BD no ato de registo
data class UnregisteredUser(
    val Username : String,
    val Password : String
)