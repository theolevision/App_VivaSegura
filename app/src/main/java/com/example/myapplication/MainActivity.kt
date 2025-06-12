package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import android.widget.TextView
import android.text.SpannableString
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.text.Spannable

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que o conteúdo vá atrás das barras do sistema
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContentView(R.layout.home)
//        val textView = findViewById<TextView>(R.id.seu_textview)
//        val texto = "Não possui conta? Registre-se"
//        val spannable = SpannableString(texto)
//        spannable.setSpan(
//            StyleSpan(Typeface.BOLD),
//            texto.indexOf("Registre-se"),
//            texto.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        textView.text = spannable
    }
}
