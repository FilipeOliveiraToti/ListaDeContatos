package com.foliveira.pacientes.listadecontatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContatosAdapter(
    val onItemClick: (contato: Contato) -> Unit,
    val onItemLongClick: (contato: Contato) -> Unit
) : RecyclerView.Adapter<ContatosAdapter.ContatosViewHolder>() {

    private val contatos = mutableListOf<Contato>()

    inner class ContatosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contato: Contato) {
            val nomeTextView = itemView.findViewById<TextView>(R.id.tv_nome)
            val idadeTextView = itemView.findViewById<TextView>(R.id.tv_idade)
            val telefoneTextView = itemView.findViewById<TextView>(R.id.tv_telefone)

            nomeTextView.text = contato.nome
            idadeTextView.text = contato.idade.toString() + " anos"
            telefoneTextView.text = contato.telefone

            itemView.setOnClickListener {
                onItemClick(contato)
            }
            itemView.setOnLongClickListener {
                onItemLongClick(contato)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatosViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contato, parent, false)
        return ContatosViewHolder(view)
    }

    override fun getItemCount(): Int = contatos.size

    override fun onBindViewHolder(holder: ContatosViewHolder, position: Int) {
        val contato = contatos[position]
        holder.bind(contato)
    }

    fun adicionarContatos(contatos: List<Contato>) {
        this.contatos.clear()
        this.contatos.addAll(contatos)
        notifyDataSetChanged()
    }
}