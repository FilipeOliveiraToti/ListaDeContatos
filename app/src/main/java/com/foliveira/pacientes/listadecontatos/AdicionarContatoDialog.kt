package com.foliveira.pacientes.listadecontatos

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class AdicionarContatoDialog : DialogFragment() {

    private lateinit var editTextNome: EditText
    private lateinit var editTextIdade: EditText
    private lateinit var editTextTelefone: EditText

    private var contato: Contato? = null

    private val isProprietario: Boolean by lazy {
        arguments?.getBoolean("isProprietario", false) ?: false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        arguments?.let {
            contato = it.getSerializable("contato") as? Contato
        }

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.novo_contato, null)

            editTextNome = view.findViewById(R.id.et_nome)
            editTextIdade = view.findViewById(R.id.et_idade)
            editTextTelefone = view.findViewById(R.id.et_telefone)

            if (contato != null) {
                editTextNome.setText(contato!!.nome)
                editTextIdade.setText(contato!!.idade.toString())
                editTextTelefone.setText(contato!!.telefone)
            }

            builder.setView(view)
                .setPositiveButton("Adicionar") { dialog, id ->
                    val contatoId = contato?.id
                    val nome = editTextNome.text.toString()
                    val idade = editTextIdade.text.toString().toIntOrNull() ?: 0
                    val telefone = editTextTelefone.text.toString()

                    if (isProprietario) {
                        (activity as? ProprietarioListener)?.onProprietarioAdicionado(contatoId, nome, idade, telefone)
                    } else {
                        (activity as? ContatoListener)?.onContatoAdicionado(contatoId, nome, idade, telefone)
                    }
                }
                .setNegativeButton("Cancelar") { dialog, id ->
                    dialog.cancel()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface ContatoListener {
        fun onContatoAdicionado(id: Int?, nome: String, idade: Int, telefone: String)
    }

    interface ProprietarioListener {
        fun onProprietarioAdicionado(id: Int?, nome: String, idade: Int, telefone: String)
    }
}