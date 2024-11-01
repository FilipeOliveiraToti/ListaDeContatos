package com.foliveira.pacientes.listadecontatos

import android.content.Context
import com.foliveira.pacientes.listadecontatos.database.AppDatabase
import com.foliveira.pacientes.listadecontatos.database.ContatoEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class MainActivityViewModel {
    private lateinit var database: AppDatabase

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

    fun pegarInformacoesContatos(): List<Contato> {
        return database.contatoDao().getAll().map { it ->
            Contato(it.id, it.nome, it.idade, it.telefone)
        }
    }

    fun adicionarContato(contato: Contato) {
       database.contatoDao().insert(
           ContatoEntity(
               nome = contato.nome,
               idade = contato.idade,
               telefone = contato.telefone
           )
       )
    }

    fun atualizarContato(contato: Contato) {
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

    fun deletarContato(contato: Contato) {
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