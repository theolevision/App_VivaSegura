package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Anotacao(val titulo: String, val descricao: String)

class AnotacoesActivity : AppCompatActivity() {
    private val anotacoes = mutableListOf<Anotacao>()
    private lateinit var container: LinearLayout
    private lateinit var busca: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anotacoes)

        container = findViewById(R.id.container)
        busca = findViewById(R.id.Busca)
        val addButton = findViewById<Button>(R.id.AddButton)

        addButton.setOnClickListener { mostrarDialogNovaAnotacao() }

        busca.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { filtrarAnotacoes(s.toString()) }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        atualizarLista()
    }

    private fun mostrarDialogNovaAnotacao() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_anotacao, null)
        val editTitulo = dialogView.findViewById<EditText>(R.id.editTitulo)
        val editDescricao = dialogView.findViewById<EditText>(R.id.editDescricao)

        AlertDialog.Builder(this)
            .setTitle("Nova Anotação")
            .setView(dialogView)
            .setPositiveButton("Salvar") { _, _ ->
                val titulo = editTitulo.text.toString()
                val descricao = editDescricao.text.toString()
                if (titulo.isNotBlank()) {
                    anotacoes.add(Anotacao(titulo, descricao))
                    atualizarLista()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun atualizarLista(filtro: String = "") {
        container.removeAllViews()
        val listaFiltrada = if (filtro.isBlank()) anotacoes else anotacoes.filter {
            it.titulo.contains(filtro, ignoreCase = true) || it.descricao.contains(filtro, ignoreCase = true)
        }
        for (anotacao in listaFiltrada) {
            val itemView = LayoutInflater.from(this).inflate(R.layout.item_anotacao, container, false)
            val tituloView = itemView.findViewById<TextView>(R.id.tituloAnotacao)
            tituloView.text = anotacao.titulo
            itemView.setOnClickListener { mostrarDialogVisualizar(anotacao) }
            container.addView(itemView)
        }
    }

    private fun mostrarDialogVisualizar(anotacao: Anotacao) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_anotacao, null)
        val editTitulo = dialogView.findViewById<EditText>(R.id.editTitulo)
        val editDescricao = dialogView.findViewById<EditText>(R.id.editDescricao)
        editTitulo.setText(anotacao.titulo)
        editDescricao.setText(anotacao.descricao)
        editTitulo.isEnabled = false
        editDescricao.isEnabled = false

        AlertDialog.Builder(this)
            .setTitle("Anotação")
            .setView(dialogView)
            .setPositiveButton("Fechar", null)
            .show()
    }

    private fun filtrarAnotacoes(filtro: String) {
        atualizarLista(filtro)
    }
}