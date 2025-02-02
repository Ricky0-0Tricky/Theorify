package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
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

class LoginActivity : AppCompatActivity (){
    // Declaração dos elementos que compõem a página
    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var loginBtn : Button
    lateinit var switch_register : TextView

    // Bearer da API
    var bearer = BuildConfig.bearer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // Captura dos elementos que compõem a página
        usernameInput = findViewById(R.id.username_login_input)
        passwordInput = findViewById(R.id.password_login_input)
        loginBtn = findViewById(R.id.login_btn)
        switch_register = findViewById(R.id.switch_register)

        // Evento "onClick" do botão de login
        loginBtn.setOnClickListener{
            // Desativa o botão
            manageButton(false)
            // Obtenção dos dados fornecidos
            val username = usernameInput.text.trim().toString().replace("\\s".toRegex(), "")
            val password = passwordInput.text.trim().toString().replace("\\s".toRegex(), "")
            // Verificação se os mesmo estão preenchidos
            if(username.isNotBlank() && password.isNotBlank()){
                // Verificação se a password está entre o limite delimitado
                if(password.length >= 4 && password.length <= 8){
                    getUser(username,password)
                } else {
                    // Reativa o botão
                    manageButton(true)
                    // Aviso ao Utilizador que a palavra-passe não tem um tamanho adequado
                    Toast.makeText(this,"A palavra-passe tem de ter um tamanho mínimo de 4 e máximo de 8... ", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Reativa o botão
                manageButton(true)
                Toast.makeText(this,"Por favor, insira um username e respetiva password",Toast.LENGTH_SHORT).show()
            }
        }

        // Evento "onClick" da TextView de registo
        switch_register.setOnClickListener{
            goRegister()
        }
    }

    /**
     * Metodo de Obtenção do User da BD para autenticação do utilizador
     */
    fun getUser(username : String, password : String){
        val call = RetrofitInitializer().apiService().getUser(bearer,username)
        call.enqueue(object : Callback<RegisteredUser> {
            override fun onResponse(call: Call<RegisteredUser>, response: Response<RegisteredUser>) {
                if(response.isSuccessful){
                    response.body()?.let {
                        // Obtenção do Utilizador na BD
                        val user : RegisteredUser = it
                        // Tentativa de Autenticação do Utilizador
                        verifyUser(password,user)
                    }
                } else {
                    // Parsing da messagem de erro
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
                    // Aviso ao Utilizador que ocorreu um erro no ato de autenticação (BD)
                    Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisteredUser>, t: Throwable?) {
                // Limpeza dos Inputs
                usernameInput.setText("")
                passwordInput.setText("")
                // Reativa o botão
                manageButton(true)
                // Aviso ao Utilizador que ocorreu um erro no ato de autenticação
                Toast.makeText(this@LoginActivity,t?.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Metodo para verificar a veracidade da password inserida contra a password da conta
     */
    fun verifyUser(password : String, user : RegisteredUser){
        if(user.Password == password){
            // Inserção do Utilizador Autenticado no companion object
            RegisteredUser.currentUser = user
            // Redirecionamento para a Página Principal
            goMainPage()
        } else {
            // Reativa o botão
            manageButton(true)
            // Aviso ao Utilizador que a password inserida não condiz com a password da conta registada
            Toast.makeText(this,"Password errada, tente novamente...",Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Metodo para trocar para a Página de Registo
     */
    fun goRegister(){
        // Criação da Nova Atividade
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        // Limpeza dos Inputs
        usernameInput.setText("")
        passwordInput.setText("")
    }

    /**
     * Metodo para, após autenticação, redirecionar para Página Principal
     */
    fun goMainPage(){
        // Criação da Nova Atividade
        val intent = Intent(this, ToolsActivity::class.java)
        startActivity(intent)
    }

    /**
     * Metodo para gerir o estado do botão de login
     */
    fun manageButton(state : Boolean) {
        if (state == true) {
            loginBtn.isEnabled = true
            loginBtn.isClickable = true
            loginBtn.setBackgroundColor(ContextCompat.getColor(loginBtn.context, R.color.white))
        } else {
            loginBtn.isEnabled = false
            loginBtn.isClickable = false
            loginBtn.setBackgroundColor(ContextCompat.getColor(loginBtn.context, R.color.greyish))
        }
    }
}