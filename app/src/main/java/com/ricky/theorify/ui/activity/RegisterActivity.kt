package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.ricky.theorify.R
import com.ricky.theorify.model.APIResult
import com.ricky.theorify.model.UnregisteredUser
import com.ricky.theorify.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity (){
    // Declaração dos elementos que compõem a página
    lateinit var usernameInput : EditText
    lateinit var passwordInput : EditText
    lateinit var rp_passwordInput : EditText
    lateinit var registerBtn : Button
    lateinit var switch_login : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // Captura dos elementos que compõem a página
        usernameInput = findViewById(R.id.username_register_input)
        passwordInput = findViewById(R.id.password_register_input)
        rp_passwordInput = findViewById(R.id.password_register_repeat_input)
        registerBtn = findViewById(R.id.register_btn)
        switch_login = findViewById(R.id.switch_login)

        // Evento "onClick" do botão de registo
        registerBtn.setOnClickListener{
            // Desativa o botão
            manageButton(false)
            // Obtenção dos dados fornecidos
            val username = usernameInput.text.trim().toString().replace("\\s".toRegex(), "")
            val password = passwordInput.text.trim().toString().replace("\\s".toRegex(), "")
            val rp_password = rp_passwordInput.text.trim().toString().replace("\\s".toRegex(), "")
            // Verificação se os mesmos foram preenchidos
            if(username.isNotBlank() && password.isNotBlank() && rp_password.isNotBlank()){
                verifiyPasswords(username,password,rp_password)
            } else {
                // Reativa o botão
                manageButton(true)
                // Aviso ao Utilizador que não preencheu todos os campos necessários
                Toast.makeText(this,"Por favor, preencha todos os campos...",Toast.LENGTH_SHORT).show()
            }
        }

        // Evento "onClick" da TextView de login
        switch_login.setOnClickListener{
            goLogin()
        }
    }

    /**
     * Metodo para verificar se ambas as passwords correspondem e possuem um tamanho adequado
     */
    fun verifiyPasswords(username : String, password : String, rp_password : String){
        // Verificação se ambas as passwords correspondem
        if(password == rp_password){
            // Verificação se ambas possuem um tamanho adequado aos limites delimitados
            if(password.length >= 4 && password.length <= 8){
                // Criação do Objeto para posterior envio para a BD
                val possibleUser : UnregisteredUser = UnregisteredUser(username, password)
                // Tentativa de Registo do Utilizador
                postUser(possibleUser)
            } else {
                // Reativa o botão
                manageButton(true)
                // Aviso ao Utilizador que as palavras-passe não cumprem com os requisitos delimitados
                Toast.makeText(this,"As palavras-passes têm de ter um tamanho mínimo de 4 e máximo de 8...", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Limpeza dos Inputs das Passwords
            passwordInput.setText("")
            rp_passwordInput.setText("")
            // Reativa o botão
            manageButton(true)
            // Aviso ao Utilizador que as palavras-passe não correspondem
            Toast.makeText(this,"As palavras-passe inseridas não correspondem...", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Metodo para tentar registar o utilizador na BD
     */
    fun postUser(possibleUser : UnregisteredUser){
        val call = RetrofitInitializer().apiService().postUser("Bearer JYmsOqJpqkWJz5gJlTcF1jkxCl39QAoJYsQDubqZMdg24Hq9B8rjPv2", possibleUser)
        call.enqueue(object : Callback<APIResult> {
            override fun onResponse(call: Call<APIResult>,
                                    response: Response<APIResult>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        // Aviso ao Utilizador que o registo foi realizado com sucesso
                        Toast.makeText(this@RegisterActivity,"Utilizador criado com sucesso!\nA redirecionar para a página de Login.",Toast.LENGTH_SHORT).show()
                        // Redirecionamento para a Página de Login
                        goLogin()
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
                    Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<APIResult>, t: Throwable) {
                // Reativa o botão
                manageButton(true)
                // Aviso ao Utilizador que ocorreu um erro no ato de registo
                Toast.makeText(this@RegisterActivity,call.execute().errorBody()?.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Metodo para trocar para a Página de Login
     */
    fun goLogin(){
        // Criação da Nova Atividade
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        // Limpeza dos Inputs
        usernameInput.setText("")
        passwordInput.setText("")
        rp_passwordInput.setText("")
    }

    /**
     * Metodo para gerir o estado do botão de registo
     */
    fun manageButton(state : Boolean) {
        if (state == true) {
            registerBtn.isEnabled = true
            registerBtn.isClickable = true
            registerBtn.setBackgroundColor(ContextCompat.getColor(registerBtn.context, R.color.white))
        } else {
            registerBtn.isEnabled = false
            registerBtn.isClickable = false
            registerBtn.setBackgroundColor(ContextCompat.getColor(registerBtn.context, R.color.greyish))
        }
    }
}