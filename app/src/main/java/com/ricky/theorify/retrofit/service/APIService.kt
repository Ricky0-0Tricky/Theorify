package com.ricky.theorify.service

import com.ricky.theorify.model.APIResult
import com.ricky.theorify.model.InstrumentNotes
import com.ricky.theorify.model.InstrumentState
import com.ricky.theorify.model.RegisteredUser
import com.ricky.theorify.model.UnregisteredUser
import com.ricky.theorify.model.isBlocked
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {
    // Pedido GET de conta do Utilizador segundo Username (Parâmetro)
    @GET("users")
    fun getUser(@Header("Authorization") authorization : String, @Query("Username") username: String): Call<RegisteredUser>

    // Pedido POST para registo de conta segundo Username e Password (JSON/BODY)
    @POST("users")
    fun postUser(@Header("Authorization") authorization : String, @Body possibleUser : UnregisteredUser): Call<APIResult>

    // Pedido DELETE para apagar conta segundo Username (Parâmetro)
    @DELETE("users")
    fun deleteUser(@Header("Authorization") authorization : String, @Query("Username") username : String): Call<APIResult>

    // Pedido GET para receber estado do instrumento segundo UtilizadorID e Name (Parâmetro)
    @GET("instrument")
    fun getInstrumentState(@Header("Authorization") authorization : String, @Query("UtilizadorID") userID : Int, @Query("Name") instrumentName : String): Call<isBlocked>

    // Pedido PATCH para alterar o isBlocked segundo o UtilizadorID e Name (Parâmetro)
    @PATCH("instrument")
    fun patchInstrumentState(@Header("Authorization") authorization : String, @Query("UtilizadorID") userID : Int, @Query("Name") instrumentName : String): Call<APIResult>

    // Pedido GET para receber os links das notas segundo o InstrumentName (Parâmetro)
    @GET("notes")
    fun getNotes(@Header("Authorization") authorization : String, @Query("InstrumentName") instrumentName : String): Call<List<String>>
}