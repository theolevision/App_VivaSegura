package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EvidenciasActivity : AppCompatActivity() {

    private lateinit var evidenciasContainer: LinearLayout

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { addEvidenciaView(it, "Imagem") }
    }
    private val pickVideo = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { addEvidenciaView(it, "Vídeo") }
    }
    private val pickAudio = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { addEvidenciaView(it, "Áudio") }
    }
    private val pickDocument = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { addEvidenciaView(it, "Documento") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.evidencias)

        evidenciasContainer = findViewById(R.id.evidenciasContainer)

        findViewById<Button>(R.id.ImagesButton).setOnClickListener {
            pickImage.launch("image/*")
        }
        findViewById<Button>(R.id.VideoButton).setOnClickListener {
            pickVideo.launch("video/*")
        }
        findViewById<Button>(R.id.AudioButton).setOnClickListener {
            pickAudio.launch("audio/*")
        }
        findViewById<Button>(R.id.DocumentButton).setOnClickListener {
            pickDocument.launch("*/*")
        }
    }

    private fun addEvidenciaView(uri: Uri, tipo: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_evidencias, evidenciasContainer, false)
        val nomeArquivo = getFileName(uri)
        view.findViewById<TextView>(R.id.evidenciaTipo).text = tipo
        view.findViewById<TextView>(R.id.evidenciaNome).text = nomeArquivo
        evidenciasContainer.addView(view)
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "Arquivo"
    }
}