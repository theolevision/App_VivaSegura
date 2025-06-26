package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PanicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.panic)

        val continueButton = findViewById<Button>(R.id.continueButton)
        continueButton.setOnClickListener {
            // Abre o app de telefone com o número 190 preenchido
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:190")
            startActivity(intent)
        }

        val cancelButton = findViewById<Button>(R.id.cancel_button)
        cancelButton.setOnClickListener {
            // Volta para a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
}