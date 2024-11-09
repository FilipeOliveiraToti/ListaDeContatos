package com.foliveira.pacientes.listadecontatos

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foliveira.pacientes.listadecontatos.database.AppDatabase
import com.foliveira.pacientes.listadecontatos.database.ContatoEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MainActivityViewModel : ViewModel() {
    private lateinit var database: AppDatabase

    private val _listaContato = MutableLiveData<List<Contato>>()
    val listaContato: LiveData<List<Contato>>
        get() = _listaContato

    fun pegarInformacoesProprietario(filesDir: File): Contato? {
        val file = File(filesDir, "proprietario.json")

        if (file.exists()) {
            val reader = FileReader(file)
            val gson = Gson()
            val listType = object : TypeToken<Contato>() {}.type
            val proprietario: Contato = gson.fromJson(reader, listType)
            return proprietario
        } else {
            return null
        }
    }

    fun adicionarInformacoesProprietario(proprietario: Contato, filesDir: File) {
        val json = Gson().toJson(proprietario)

        FileWriter("$filesDir/proprietario.json").use { fileWriter ->
            fileWriter.write(json)
        }
    }

    fun inicializarDatabase(context: Context) {
        database = AppDatabase.getDatabase(context)
    }

    fun pegarInformacoesContatos(){
        viewModelScope.launch(Dispatchers.IO) {
            database.contatoDao().getAll().collect{ it->
                val contatoConvertido = it.map {
                    Contato(
                        id = it.id,
                        nome = it.nome,
                        idade = it.idade,
                        telefone = it.telefone
                    )
                }

                _listaContato.postValue(contatoConvertido)
            }
        }
    }

    fun adicionarContato(contato: Contato) {
        viewModelScope.launch(Dispatchers.IO) {
            database.contatoDao().insert(
                ContatoEntity(
                    nome = contato.nome,
                    idade = contato.idade,
                    telefone = contato.telefone
                )
            )
        }
    }

    fun atualizarContato(contato: Contato) {
        viewModelScope.launch(Dispatchers.IO) {
            contato.id?.let {
                val contato = ContatoEntity(
                    id = it,
                    nome = contato.nome,
                    idade = contato.idade,
                    telefone = contato.telefone
                )
                database.contatoDao().update(
                    contato
                )
            }
        }
    }

    fun deletarContato(contato: Contato) {
        viewModelScope.launch(Dispatchers.IO) {
            contato.id?.let {
                val contato = ContatoEntity(
                    id = it,
                    nome = contato.nome,
                    idade = contato.idade,
                    telefone = contato.telefone
                )
                database.contatoDao().delete(
                    contato
                )
            }
        }
    }
}