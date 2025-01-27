package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
            // Obtenção dos dados fornecidos
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val rp_password = rp_passwordInput.text.toString()
            // Verificação se os mesmos foram preenchidos
            if(username.isNotBlank() && password.isNotBlank() && rp_password.isNotBlank()){
                verifiyPasswords(username,password,rp_password)
            } else {
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
     * Metodo para verificar se ambas as passwords correspondem
     */
    fun verifiyPasswords(username : String, password : String, rp_password : String){
        if(password == rp_password){
            // Criação do Objeto para posterior envio para a BD
            var possibleUser : UnregisteredUser = UnregisteredUser(username, password)
            // Tentativa de Registo do Utilizador
            postUser(possibleUser)
        } else {
            // Limpeza dos Inputs das Passwords
            passwordInput.setText("")
            rp_passwordInput.setText("")
            // Aviso ao Utilizador que as palavras-passe não correspondem
            Toast.makeText(this,"As palavras-passe inseridas não correspondem...", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Metodo para tentar registar o utilizador na BD
     */
    fun postUser(possibleUser : UnregisteredUser){
        val call = RetrofitInitializer().apiService().postUser("", possibleUser)
        call.enqueue(object : Callback<APIResult> {
            override fun onResponse(call: Call<APIResult>,
                                    response: Response<APIResult>
            ) {
                response?.body()?.let {
                    // Aviso ao Utilizador que o registo foi realizado com sucesso
                    Toast.makeText(this@RegisterActivity,"Utilizador criado com sucesso!\nA redirecionar para a página de Login.",Toast.LENGTH_SHORT).show()
                    // Redirecionamento para a Página de Login
                    setContentView(R.layout.login_page)
                }
            }

            override fun onFailure(call: Call<APIResult>, t: Throwable) {
                // Aviso ao Utilizador que ocorreu um erro no ato de registo
                Toast.makeText(this@RegisterActivity,call.execute().errorBody()?.toString(),Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Metodo para trocar para a Página de login
     */
    fun goLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}