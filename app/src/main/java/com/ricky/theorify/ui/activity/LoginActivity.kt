package com.ricky.theorify.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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

        // Cópia das notas de piano para armazenamento interno
        movePianoNotes(this)

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

        // Evento "onBackPressedDispatcher" do telemóvel
        onBackPressedDispatcher.addCallback(this){
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
        val intent = Intent(this, MainPageActivity::class.java)
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

    /**
     * Metodo para copiar notas de raw para armazenamento interno
     */
    private fun movePianoNotes(context: Context) {
        // Captura a diretória necessária
        val pianoDir = File(context.filesDir, "Piano")
        // Verificação da sua existência
        if (pianoDir.exists()) {
            // Não faz nada
            return
        } else {
            // Cria a pasta
            pianoDir.mkdirs()
            // Nomes dos Ficheiros
            val rawFileNames = arrayOf("a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "asharp0", "asharp1", "asharp2", "asharp3", "asharp4", "asharp5", "asharp6", "asharp7", "b0", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "d1", "d2", "d3", "d4", "d5", "d6", "d7", "dflat1", "dflat2", "dflat3", "dflat4", "dflat5", "dflat6", "dflat7", "dsharp1", "dsharp2", "dsharp3", "dsharp4", "dsharp5", "dsharp6", "dsharp7", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "g1", "g2", "g3", "g4", "g5", "g6", "g7", "gflat1", "gflat2", "gflat3", "gflat4", "gflat5", "gflat6", "gflat7", "gsharp1", "gsharp2", "gsharp3", "gsharp4", "gsharp5", "gsharp6", "gsharp7")
            // Contador
            var i : Int = 0
            // Faz cópia dos ficheiros raw para armazenamento interno
            for (rawFileName in rawFileNames) {
                // Busca o ID do ficheiro
                val rawResId = context.resources.getIdentifier(rawFileName, "raw", context.packageName)

                // Se não encontrar, continua
                if (rawResId == 0) {
                    continue
                }
                val newRawFileNames = arrayOf("A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "Asharp0", "Asharp1", "Asharp2", "Asharp3", "Asharp4", "Asharp5", "Asharp6", "Asharp7", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "Dflat1", "Dflat2", "Dflat3", "Dflat4", "Dflat5", "Dflat6", "Dflat7", "Dsharp1", "Dsharp2", "Dsharp3", "Dsharp4", "Dsharp5", "Dsharp6", "Dsharp7", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "G1", "G2", "G3", "G4", "G5", "G6", "G7", "Gflat1", "Gflat2", "Gflat3", "Gflat4", "Gflat5", "Gflat6", "Gflat7", "Gsharp1", "Gsharp2", "Gsharp3", "Gsharp4", "Gsharp5", "Gsharp6", "Gsharp7")
                // Altera o nome do ficheiro
                val outFile = File(pianoDir, newRawFileNames.get(i))

                // Se este já existir, continua
                if (outFile.exists()) {
                    continue
                }

                // Realiza a cópia dos ficheiros em raw para armazenamento interno
                try {
                    context.resources.openRawResource(rawResId).use { `in` ->
                        FileOutputStream(outFile).use { out ->
                            val buffer = ByteArray(1024)
                            var read: Int
                            while ((`in`.read(buffer).also { read = it }) != -1) {
                                out.write(buffer, 0, read)
                            }
                        }
                    }
                    i++
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * Verifica a permissão de gravação de áudio
     */
    private fun checkPermissions() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                return
            }

            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                Toast.makeText(this, "Permissões de Armazenamento externo são necessárias!", Toast.LENGTH_LONG).show()
            }

            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                return
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                Toast.makeText(this, "Permissões de Armazenamento externo são necessárias!", Toast.LENGTH_LONG).show()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    /**
     * Metodo para pedir permissões de gravação de áudio
     */
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permissão fornecida!", Toast.LENGTH_LONG)
        } else {
            Toast.makeText(this, "Permissão Negada...", Toast.LENGTH_LONG).show()
        }
    }
}