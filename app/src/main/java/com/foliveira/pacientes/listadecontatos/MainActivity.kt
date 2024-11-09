package com.foliveira.pacientes.listadecontatos

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), AdicionarContatoDialog.ContatoListener, AdicionarContatoDialog.ProprietarioListener {
    val tvNomeProprietario : TextView by lazy { findViewById(R.id.tv_nome_proprietario) }
    val tvIdadeProprietario : TextView by lazy { findViewById(R.id.tv_idade_proprietario) }
    val tvTelefoneProprietario : TextView by lazy { findViewById(R.id.tv_telefone_proprietario) }
    val cardiviewProprietario : CardView by lazy { findViewById(R.id.cv_proprietario) }
    val tvSemProprietario : TextView by lazy { findViewById(R.id.tv_sem_proprietario) }
    val fabAdicionarContato : FloatingActionButton by lazy { findViewById(R.id.fab_adicionar_contato) }
    val recyclerView : RecyclerView by lazy { findViewById(R.id.rv_contatos) }

    private lateinit var adapter: ContatosAdapter

    val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initializeDatabase()
        viewModel.pegarInformacoesContatos()
        initializeObservers()
        setupInfoProprietario()
        setupRecyclerView()
        setupFab()
    }

    private fun initializeObservers() {
        viewModel.listaContato.observe(this) { contatos ->
            contatos?.let {
                adapter.adicionarContatos(contatos)
            }
        }
    }

    private fun initializeDatabase() {
        viewModel.inicializarDatabase(this@MainActivity)
    }


    private fun setupInfoProprietario() {
        val proprietatio = viewModel.pegarInformacoesProprietario(filesDir)

        proprietatio?.let {
            tvNomeProprietario.text = it.nome
            tvIdadeProprietario.text = it.idade.toString() + " anos"
            tvTelefoneProprietario.text = it.telefone
            cardiviewProprietario.visibility = CardView.VISIBLE
            tvSemProprietario.visibility = TextView.GONE
        } ?: run {
            cardiviewProprietario.visibility = CardView.GONE
            tvSemProprietario.visibility = TextView.VISIBLE
            tvSemProprietario.setOnClickListener{
                abrirTelaAdicionarContato(true)
            }
        }
    }

    private fun setupRecyclerView() {
         adapter = ContatosAdapter(
            onItemClick = { contato ->
                abrirTelaAdicionarContato(false, contato)
            },
            onItemLongClick = {
                viewModel.deletarContato(it)
            }
        )
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        fabAdicionarContato.setOnClickListener {
            abrirTelaAdicionarContato(false)
        }
    }

    private fun abrirTelaAdicionarContato(isProprietario: Boolean, contato: Contato? = null) {
        val dialog = AdicionarContatoDialog()
        dialog.arguments = Bundle().apply {
            putBoolean("isProprietario", isProprietario)
            contato?.let {
                putSerializable("contato", it)
            }
        }
        dialog.show(supportFragmentManager, "AdicionarContatoDialog")
    }

    override fun onContatoAdicionado(id: Int?, nome: String, idade: Int, telefone: String) {
        val contato = Contato(id = id, nome = nome, idade = idade, telefone = telefone)
        if (contato.id == null) {
            viewModel.adicionarContato(contato)
        } else {
            viewModel.atualizarContato(contato)

        }
    }

    override fun onProprietarioAdicionado(id: Int?, nome: String, idade: Int, telefone: String) {
        val proprietario = Contato(id = id, nome = nome, idade = idade, telefone = telefone)
        viewModel.adicionarInformacoesProprietario(proprietario, filesDir)
        setupInfoProprietario()
    }
}