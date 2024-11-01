package com.foliveira.pacientes.listadecontatos

import java.io.Serializable

data class Contato(
    val id: Int? = null,
    val nome: String,
    val idade: Int,
    val telefone: String
) : Serializable
