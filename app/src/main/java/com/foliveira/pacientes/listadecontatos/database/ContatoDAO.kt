package com.foliveira.pacientes.listadecontatos.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ContatoDAO {
    @Query("SELECT * FROM ContatoEntity ORDER BY nome ASC")
    fun getAll(): List<ContatoEntity>

    @Update
    fun update(contato: ContatoEntity)

    @Delete
    fun delete(contato: ContatoEntity)

    @Insert
    fun insert(contato: ContatoEntity)
}