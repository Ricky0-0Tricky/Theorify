package com.ricky.theorify.ui.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.ricky.theorify.BuildConfig
import com.ricky.theorify.R
import com.ricky.theorify.model.APIResult
import com.ricky.theorify.model.InstrumentNotes
import com.ricky.theorify.model.InstrumentState
import com.ricky.theorify.model.PickedInstrument
import com.ricky.theorify.model.RegisteredUser
import com.ricky.theorify.model.isBlocked
import com.ricky.theorify.retrofit.RetrofitInitializer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL


class InstrumentActivity : AppCompatActivity() {
    // Declaração dos elementos que compõem a página
    lateinit var pianoBtn : ImageButton
    lateinit var acousticBtn : ImageButton
    lateinit var electricBtn : ImageButton
    private var mediaPlayer: MediaPlayer? = null

    // Bearer da API
    var bearer = BuildConfig.bearer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.instrument_page)

        // Captura dos elementos que compõem a página
        pianoBtn = findViewById(R.id.piano_imagem)
        acousticBtn = findViewById(R.id.acustica_imagem)
        electricBtn = findViewById(R.id.eletrica_imagem)

        // Criação das Diretórias para as Notas dos Instrumentos
        createInstrumentDirectories(this)

        // Evento "onBackPressedDispatcher" do telemóvel
        this.onBackPressedDispatcher.addCallback(this) {}

        // Evento "onClick" do botão do piano
        pianoBtn.setOnClickListener {
            // Verifica a conectividade
            if(checkInternetAccess()) {
                // Desativa os botões
                manageButtons(false)
                // Toca a intro do instrumento
                playIntro("Piano")
                // Define o instrumento escolhido
                PickedInstrument.currentInstrument = "Piano"
                // Avisa o Utilizador da escolha
                Toast.makeText(this, "Piano escolhido!", Toast.LENGTH_SHORT).show()
                // Reativa os botões
                manageButtons(true)
            } else {
                // Volta à página de login caso não exista conectividade
                exit()
            }
        }

        // Evento "onClick" do botão da guitarra acústica
        acousticBtn.setOnClickListener {
            // Verifica a conectividade
            if(checkInternetAccess()){
                // Desativa os botões
                manageButtons(false)
                // Toca a intro do instrumento
                playIntro("Acoustic_Guitar")
                // Cria um objeto do tipo InstrumentoState para análise do estado do mesmo
                var instrumentState: InstrumentState = InstrumentState(RegisteredUser.currentUser!!.ID, "Acoustic_Guitar")
                getInstrumentState(instrumentState)
            } else {
                // Volta à página de login caso não exista conectividade
                exit()
            }
        }

        // Evento "onClick" do botão da guitarra elétrica
        electricBtn.setOnClickListener {
            // Verifica a conectividade
            if(checkInternetAccess()){
                // Desativa os botões
                manageButtons(false)
                // Toca a intro do instrumento
                playIntro("Electric_Guitar")
                // Cria um objeto do tipo InstrumentState para análise do estado do mesmo
                var instrumentState: InstrumentState = InstrumentState(RegisteredUser.currentUser!!.ID, "Electric_Guitar")
                // Verifica se existe acesso à Internet
                getInstrumentState(instrumentState)
            } else {
                // Volta à página de login caso não exista conectividade
                exit()
            }
        }
    }

    /**
     * Metodo de Obtenção do Estado do Instrumento escolhido
     */
    fun getInstrumentState(instrumentState: InstrumentState) {
        val call = RetrofitInitializer().apiService().getInstrumentState(bearer, instrumentState.UtilizadorID, instrumentState.Name)
        call.enqueue(object : Callback<isBlocked> {
            override fun onResponse(call: Call<isBlocked>, response: Response<isBlocked>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val state: isBlocked = it
                        // Verifica a conectividade
                        if(checkInternetAccess()){
                            processInstrumentState(state.isBlocked, instrumentState.Name, instrumentState)
                        } else {
                            // Volta à página de login caso não exista conectividade
                            exit()
                        }
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()?.let { errorBody ->
                        try {
                            val gson = Gson()
                            val errorContents = gson.fromJson(errorBody, APIResult::class.java)
                            errorContents.result
                        } catch (e: Exception) {
                            "Erro inesperado: ${response.code()}"
                        }
                    } ?: "Erro inesperado: ${response.code()}"
                    // Avisa o Utilizador da ocorrência de um erro
                    Toast.makeText(this@InstrumentActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    // Verifica a conectividade
                    if(checkInternetAccess()){
                        // Reativa os botões
                        manageButtons(true)
                    } else {
                        // Volta à página de login caso não exista conectividade
                        exit()
                    }
                }
            }
            override fun onFailure(call: Call<isBlocked>, t: Throwable) {
                // Avisa o Utilizador da ocorrência de um erro
                Toast.makeText(this@InstrumentActivity, "Falha de rede", Toast.LENGTH_SHORT).show()
                // Verifica conectividade
                if(checkInternetAccess()){
                    // Reativa os botões
                    manageButtons(true)
                } else {
                    // Volta à página de login caso não exista conectividade
                    exit()
                }
            }
        })
    }

    /**
     * Metodo para processar o estado do instrumento
     */
    fun processInstrumentState(usersState: Int, instrumentName: String, instrumentState: InstrumentState) {
        if (usersState == 1) {
            // Realiza uma questão ao Utilizador para desbloquear o instrumento escolhido
            if (checkInternetAccess()){
                askQuestion(instrumentState)
            } else {
                // Volta à página de login caso não exista conectividade
                exit()
            }
        } else {
            // Verifica a conectividade
            if(checkInternetAccess()) {
                // Seleciona o instrumento
                PickedInstrument.currentInstrument = instrumentName
                // Determina o número de ficheiros armazenados na diretória do instrumento
                val instrumentFiles: Int = countInstrumentFiles(this, instrumentName)
                // Verifica se o telemóvel possui todos as notas do instrumento
                if (instrumentFiles < 45) {
                    deleteInstrumentFiles(this, instrumentName)
                    patchInstrumentState(instrumentState)
                }
                when(PickedInstrument.currentInstrument){
                    "Acoustic_Guitar" -> {
                        // Avisa o Utilizador que o Instrumento foi escolhido
                        Toast.makeText(this,"Guitarra acústica escolhida!", Toast.LENGTH_LONG).show()
                        // Reativa os botões
                        manageButtons(true)
                    }
                    "Electric_Guitar" -> {
                        // Avisa o Utilizador que o Instrumento foi escolhido
                        Toast.makeText(this,"Guitarra elétrica escolhida!", Toast.LENGTH_LONG).show()
                        // Reativa os botões
                        manageButtons(true)
                    }
                }
            } else {
                // Volta à página de login caso não exista conectividade
                exit()
            }
        }
    }

    /**
     * Metodo para desbloquear o instrumento
     */
    fun patchInstrumentState(instrumentState: InstrumentState) {
        val call = RetrofitInitializer().apiService().patchInstrumentState(bearer, instrumentState.UtilizadorID, instrumentState.Name)
        call.enqueue(object : Callback<APIResult> {
            override fun onResponse(call: Call<APIResult>, response: Response<APIResult>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        // Verificação de conectividade
                        if(checkInternetAccess()){
                            getNotes(instrumentState.Name)
                        } else {
                            // Volta à página de login caso não exista conectividade
                            exit()
                        }
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()?.let { errorBody ->
                        try {
                            val gson = Gson()
                            val errorContents = gson.fromJson(errorBody, APIResult::class.java)
                            errorContents.result
                        } catch (e: Exception) {
                            "Erro inesperado: ${response.code()}"
                        }
                    } ?: "Erro inesperado: ${response.code()}"
                    // Avisa o Utilizador da ocorrência de um erro
                    Toast.makeText(this@InstrumentActivity, errorMessage, Toast.LENGTH_LONG).show()
                    // Verificação de conectividade
                    if(checkInternetAccess()){
                        // Reativa os botões
                        manageButtons(true)
                    } else {
                        // Volta à página de login caso não exista conectividade
                        exit()
                    }
                }
            }
            override fun onFailure(call: Call<APIResult>, t: Throwable) {
                // Avisa o Utilizador da ocorrência de um erro
                Toast.makeText(this@InstrumentActivity, "Falha de rede", Toast.LENGTH_LONG).show()
                // Verificação de conectividade
                if(checkInternetAccess()){
                    // Reativa os botões
                    manageButtons(true)
                } else {
                    // Volta à página de login caso não exista conectividade
                    exit()
                }
            }
        })
    }

    /**
     * Metodo para obtenção dos URLs das notas do instrumento
     */
    fun getNotes(instrumentName: String) {
        val call = RetrofitInitializer().apiService().getNotes(bearer, instrumentName)
        call.enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    response.body()?.let { notesList ->
                        val notes = InstrumentNotes(notesList)
                        // Verificação de conectividade
                        if(checkInternetAccess()){
                            downloadNotes(notes, PickedInstrument.currentInstrument)
                        } else {
                            // Volta à página de login caso não exista conectividade
                            exit()
                        }
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()?.let { errorBody ->
                        try {
                            val gson = Gson()
                            val errorContents = gson.fromJson(errorBody, APIResult::class.java)
                            errorContents.result
                        } catch (e: Exception) {
                            "Erro inesperado: ${response.code()}"
                        }
                    } ?: "Erro inesperado: ${response.code()}"
                    // Avisa o Utilizador da ocorrência de um erro
                    Toast.makeText(this@InstrumentActivity, errorMessage, Toast.LENGTH_LONG).show()
                    // Verificação de conectividade
                    if(checkInternetAccess()){
                        // Reativa os botões
                        manageButtons(true)
                    } else {
                        // Volta à página de login caso não exista conectividade
                        exit()
                    }
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Avisa o Utilizador da ocorrência de um erro
                Toast.makeText(this@InstrumentActivity, "Falha de rede: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                // Verificação de conectividade
                if(checkInternetAccess()){
                    // Reativa os botões
                    manageButtons(true)
                } else {
                    // Volta à página de login caso não exista conectividade
                    exit()
                }
            }
        })
    }

    /**
     * Metodo para fazer download das notas segundo os URLs dados e respetivo armazenamento
     */
    fun downloadNotes(Notes : InstrumentNotes, instrumentName : String){
        // Avisa o Utilizador que o download começou
        Toast.makeText(this,"A iniciar o download das Notas do Instrumento" + PickedInstrument.currentInstrument,Toast.LENGTH_LONG).show()
        // Criação de uma Thread para lidar com o download
        Thread {
            // Acesso à pasta do instrumento escolhido
            val dir = File(applicationContext.filesDir, instrumentName)
            // Loop para download de cada nota
            for (noteUrl in Notes.instrumentNotes!!) {
                try {
                    // Busca o link e realiza a conexão com o mesmo
                    val url = URL(noteUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.connect()
                    val inputStream: InputStream = connection.inputStream
                    val fileName = noteUrl.substring(noteUrl.lastIndexOf("/") + 1)
                    val file = File(dir, fileName)
                    // Escrita da Stream de Input para um ficheiro
                    val outputStream: OutputStream = file.outputStream()
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } != -1) {
                        outputStream.write(buffer, 0, length)
                    }
                    // Fecha as streams
                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                } catch (e: Exception) {
                    // Printstack do erro
                    e.printStackTrace()
                }
            }
            runOnUiThread {
                // Avisa o Utilizador que as notas foram descarregadas
                Toast.makeText(applicationContext, "Notas Descarregadas!", Toast.LENGTH_SHORT).show()
                // Verificação da conectividade
                if(checkInternetAccess()){
                    // Reativa os botões
                    manageButtons(true)
                } else {
                    // Volta à página de login caso não exista conectividade
                    exit()
                }
            }
        }.start()
    }

    /**
     * Metodo para tocar as intros do instrumento selecionado
     */
    fun playIntro(instrumentName : String) {
        // Realiza prevenção de overlaps de intros
        mediaPlayer?.release()
        mediaPlayer = null
        // Decide a Intro a tocar
        val introFile = when (instrumentName) {
            "Piano" -> R.raw.intropiano
            "Acoustic_Guitar" -> R.raw.introacoustic
            "Electric_Guitar" -> {
                R.raw.introelectric
            }
            else -> return
        }
        // Inicializa o Media Player e toca a intro
        mediaPlayer = MediaPlayer.create(this, introFile).apply {
            start()
            setOnCompletionListener {
                release()
                mediaPlayer = null
                manageButtons(true)
            }
        }
    }

    /**
     * Metodo para fazer questão ao Utilizador para desbloquear o instrumento
     */
    fun askQuestion(instrumentState: InstrumentState) {
        // Captura do nome do instrumento
        val instrumentName = instrumentState.Name
        // Apresenta um AlertDialog para o Utilizador responder a uma questão
        when(instrumentName){
            "Acoustic_Guitar" -> {
                // Criação da questão e respetiva resposta
                val question : String = "Que género português, caracterizado como um estilo de desabafo, é sempre acompanhado com guitarra acústica?"
                val answer : String = "Fado"
                // Construção do Alert Dialog
                buildAlert(question, answer, instrumentState)
            }
            "Electric_Guitar" -> {
                // Criação da questão e respetiva resposta
                val question : String = "Que género foi extremamente popular durante os anos 80 caracterizado por estilos de cabelo encaraculados e muita make-up?"
                val answer : String = "Glam Rock"
                // Construção do Alert Dialog
                buildAlert(question, answer, instrumentState)
            }
        }
    }

    /**
     * Metodo para construir o Alert Dialog
     */
    fun buildAlert(question : String, answer : String, instrumentState: InstrumentState){
        // Criação de um AlertDialog com a questão
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desbloqueio de Instrumento")
        builder.setMessage(question)
        // Criação de um EditText para input do Utilizador
        val input = EditText(this)
        builder.setView(input)
        // Criação dos botões do AlertDialog
        builder.setPositiveButton("Submeter") { dialog, _ ->
            // Captura da resposta do Utilizador
            var userAnswer = input.text.toString().trim()
            // Verificação se a resposta está correta
            var isAnswerCorrect = userAnswer.trim().equals(answer, ignoreCase = true)
            if (isAnswerCorrect) {
                // Desbloqueio do instrumento
                patchInstrumentState(instrumentState)
            } else {
                // Avisa o Utilizador que a resposta está errada
                Toast.makeText(this,"Resposta incorreta. Tente novamente...", Toast.LENGTH_LONG).show()
            }
            // Fecha o AlertDialog
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            // Fecha o AlertDialog
            dialog.dismiss()
        }
        // Mostra o AlertDialog
        builder.show()
    }

    /**
     * Metodo para verificar a conectividade do Utilizador
     */
    fun checkInternetAccess() : Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Metodo para voltar ao Login caso não exista conectividade
     **/
    fun exit() {
        // Reposição do valor por defeito do instrumento e utilizador
        PickedInstrument.currentInstrument = "Piano"
        RegisteredUser.currentUser = null
        // Aviso ao Utilizador do redirecionamento
        Toast.makeText(this,"Não existe conectividade...A redireciona-lo para a página de login",Toast.LENGTH_LONG).show()
        // Redirecionamento para a página de login
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    /**
     * Metodo para gerir o estados do botões
     */
    fun manageButtons(state : Boolean){
        pianoBtn.isEnabled = state
        pianoBtn.isClickable = state
        acousticBtn.isEnabled = state
        acousticBtn.isClickable = state
        electricBtn.isEnabled = state
        electricBtn.isClickable = state
    }

    /**
     * Metodo para criar as pastas dos instrumentos
     */
    fun createInstrumentDirectories(context: Context) {
        // Nomeação das pastas das notas a guardar segundo o instrumento
        val acousticDir = File(context.filesDir, "Acoustic_Guitar")
        val electricDir = File(context.filesDir, "Electric_Guitar")
        // Verificação se as pastas existem ou não
        if (!acousticDir.exists()){
            // Cria a pasta para a guitarra acústica caso não exista
            acousticDir.mkdirs()
        }
        if (!electricDir.exists()){
            // Cria a pasta para a guitarra elétrica caso não existe
            electricDir.mkdirs()
        }
    }

    /**
     * Metodo para contar o número de ficheiros do instrumento escolhido
     */
    fun countInstrumentFiles(context : Context, instrumentName : String) : Int{
        // Encontra a diretória do instrumento em questão
        val dir = File(context.filesDir, instrumentName)
        // Filtra os ficheiros pelo tipo .wav
        val files = dir.listFiles { _, name -> name.endsWith(".wav") }
        // Retorna o número de ficheiros presentes
        return files?.size ?: 0
    }

    /**
     * Metodo para apagar os ficheiros do instrumento
     */
    fun deleteInstrumentFiles(context : Context, instrumentName : String){
        // Encontra a diretória do instrumento em questão
        val dir = File(context.filesDir, instrumentName)
        // Apaga os ficheiros existentes na pasta
        if(dir.exists() && dir.isDirectory){
            dir.listFiles()?.forEach { file ->
                file.delete()
            }
        }
    }
}