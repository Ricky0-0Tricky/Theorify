package com.ricky.theorify.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R
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
            // Obtenção dos dados fornecidos
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            // Verificação se os mesmo estão preenchidos
            if(username.isNotBlank() && password.isNotBlank()){
                getUser(username,password)
            } else {
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
        val call = RetrofitInitializer().apiService().getUser("",username)
        call.enqueue(object : Callback<RegisteredUser> {
            override fun onResponse(call: Call<RegisteredUser>,
                                    response: Response<RegisteredUser>) {
                response?.body()?.let {
                    // Obtenção do Utilizador na BD
                    val user : RegisteredUser = it
                    // Tentativa de Autenticação do Utilizador
                    verifyUser(password,user);
                }
            }

            override fun onFailure(call: Call<RegisteredUser>, t: Throwable?) {
                // Limpeza dos Inputs
                usernameInput.setText("")
                passwordInput.setText("")
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
            // Redirecionamento para a Página Principal
            setContentView(R.layout.rythm_page)
        } else {
            // Aviso ao Utilizador que a password inserida não condiz com a password da conta registada
            Toast.makeText(this,"Password errada, tente novamente...",Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Metodo para trocar para a Página de Registo
     */
    fun goRegister(){
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}