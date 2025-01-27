package com.ricky.theorify.model

import com.google.gson.annotations.SerializedName

data class APIResult (
    @SerializedName("message") val result: String
)