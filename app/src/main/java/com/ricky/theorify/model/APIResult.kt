package com.ricky.theorify.model

import com.google.gson.annotations.SerializedName

// Modelo da Resposta da API relativamente à tentativa de registo
data class APIResult (
    @SerializedName("message") val result: String
)