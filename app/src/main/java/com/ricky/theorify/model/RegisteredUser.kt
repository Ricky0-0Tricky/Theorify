package com.ricky.theorify.model

// Modelo da Resposta da API relativamente ao registo/login
data class RegisteredUser(
    val ID : Int,
    val Username : String,
    val Password : String
) {
    // Objeto que guardará o utilizador se o mesmo existir e as credênciais corresponderem
    companion object {
        var currentUser: RegisteredUser? = null;
    }
}
