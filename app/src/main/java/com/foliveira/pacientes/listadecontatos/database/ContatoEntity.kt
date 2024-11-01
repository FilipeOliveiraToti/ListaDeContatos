package com.foliveira.pacientes.listadecontatos.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContatoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val idade: Int,
    val telefone: String
)