package com.ricky.theorify.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.ricky.theorify.BuildConfig
import com.ricky.theorify.R
import com.ricky.theorify.model.APIResult
import com.ricky.theorify.model.RegisteredUser
import com.ricky.theorify.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageActivity : AppCompatActivity() {
    // Declaração dos elementos que compõem a página
    lateinit var profileIcon : ImageView
    lateinit var usersUsername : TextView
    lateinit var usersPassword : TextView
    lateinit var showPasswordBtn : ImageButton
    lateinit var deleteBtn : Button
    lateinit var logoutBtn : Button
    lateinit var notebutton : ImageButton
    lateinit var pausabutton : ImageButton
    lateinit var instrumentobutton : ImageButton
    lateinit var homebutton : ImageButton
    lateinit var clavebutton : ImageButton
    lateinit var toolbutton : ImageButton
    lateinit var profilebutton : ImageButton

    // Bearer da API
    var bearer = BuildConfig.bearer

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_page)

        // Captura dos elementos que compõem a página
        profileIcon = findViewById(R.id.profileImage)
        usersUsername = findViewById(R.id.username)
        usersPassword = findViewById(R.id.password)
        showPasswordBtn = findViewById(R.id.password_visibility_button)
        deleteBtn = findViewById(R.id.delete_account_btn)
        logoutBtn = findViewById(R.id.back_btn)
        notebutton = findViewById(R.id.note_button)
        pausabutton = findViewById(R.id.pausa_button)
        instrumentobutton = findViewById(R.id.instrumento_button)
        homebutton = findViewById(R.id.home_button)
        clavebutton = findViewById(R.id.clave_button)
        toolbutton = findViewById(R.id.tools_button)
        profilebutton = findViewById(R.id.profile_button)

        // Escrita do Username do Utilizador no respetivo campo
        usersUsername.setText(usersUsername.text.toString() + " " + RegisteredUser.currentUser!!.Username)

        // Var para controlar a visibilidade da password
        var state : Boolean = false

        // Evento "onClick" do botão de mostrar/esconder password da conta autenticada
        showPasswordBtn.setOnClickListener {
            state = !state
            managePasswordVisibility(state)
        }

        // Evento "onClick" do botão de apagar conta autenticada
        deleteBtn.setOnClickListener {
            manageButton(false)
            deleteUser()
        }

        // Evento "onClick" do botão de sair da conta autenticada
        logoutBtn.setOnClickListener {
            logout()
        }

        notebutton.setOnClickListener{
            val intent = Intent(this, NoteActivity::class.java)
            startActivity(intent)
        }

        pausabutton.setOnClickListener{
            val intent = Intent(this, PauseActivity::class.java)
            startActivity(intent)
        }

        instrumentobutton.setOnClickListener{
            val intent = Intent(this, InstrumentActivity::class.java)
            startActivity(intent)
        }

        homebutton.setOnClickListener{
            val intent = Intent(this, MainPageActivity::class.java)
            startActivity(intent)
        }

        clavebutton.setOnClickListener{
            val intent = Intent(this, ClaveActivity::class.java)
            startActivity(intent)
        }

        toolbutton.setOnClickListener{
            val intent = Intent(this, ToolsActivity::class.java)
            startActivity(intent)
        }
    }
    /**
     * Metodo para apagar conta na BD
     */
    fun deleteUser() {
        val call = RegisteredUser.currentUser?.let { RetrofitInitializer().apiService().deleteUser(bearer, it.Username) }
        call?.enqueue(object : Callback<APIResult> {
            override fun onResponse(call: Call<APIResult>,
                                    response: Response<APIResult>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        // Aviso ao Utilizador que o registo foi realizado com sucesso
                        Toast.makeText(this@ManageActivity,"Utilizador apagado com sucesso!\nA redirecionar para a página de Login.",
                            Toast.LENGTH_SHORT).show()
                        // Redirecionamento para a Página de Login
                        logout()
                    }
                } else {
                    // Parsing da mensagem de erro
                    val errorMessage  = response.errorBody()?.string()?.let { errorBody ->
                        try {
                            // Captura do JSON com descrição da mensagem
                            val gson = Gson()
                            val errorContents = gson.fromJson(errorBody, APIResult::class.java)
                            errorContents.result
                        } catch (e: Exception) {
                            "Erro inesperado: ${response.code()}"
                        }
                    } ?: "Erro inesperado: ${response.code()}"
                    // Reativa o botão
                    manageButton(true)
                    // Aviso ao Utilizador que ocorreu um erro no ato de registo (BD)
                    Toast.makeText(this@ManageActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResult>, t: Throwable) {
                // Reativa o botão
                manageButton(true)
                // Aviso ao Utilizador que ocorreu um erro no ato de registo
                Toast.makeText(this@ManageActivity,call.execute().errorBody()?.toString(),
                    Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Metodo para sair da conta autenticada
     */
    fun logout(){
        // Redeclaração do Utilizador Atual como nulo
        RegisteredUser.currentUser = null
        // Criação da Nova Atividade
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
    * Metodo para gerir a visibilidade da Password do Utilizador
    */
    fun managePasswordVisibility(state : Boolean){
        // Se verdadeiro mostra password, senão não mostra
        if(state == true){
            usersPassword.setText(getString(R.string.password_account_shown) + " " + RegisteredUser.currentUser!!.Password)
            showPasswordBtn.setImageResource(R.drawable.hidepassword)
        } else {
            usersPassword.setText(getString(R.string.password_account_hidden))
            showPasswordBtn.setImageResource(R.drawable.showpassword)
        }
    }
    /**
     * Metodo para gerir o estado do botão de apagar conta
     */
    fun manageButton(state : Boolean) {
        if (state == true) {
            deleteBtn.isEnabled = true
            deleteBtn.isClickable = true
            deleteBtn.setBackgroundColor(ContextCompat.getColor(deleteBtn.context, R.color.white))
        } else {
            deleteBtn.isEnabled = false
            deleteBtn.isClickable = false
            deleteBtn.setBackgroundColor(ContextCompat.getColor(deleteBtn.context, R.color.greyish))
        }
    }
}