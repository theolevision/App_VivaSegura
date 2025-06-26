package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        auth = FirebaseAuth.getInstance()

        val emailEdit = findViewById<EditText>(R.id.email)
        val passwordEdit = findViewById<EditText>(R.id.senha)
        val confirmPasswordEdit = findViewById<EditText>(R.id.confirm_password)
        val nomeEdit = findViewById<EditText>(R.id.login)
        val registerButton = findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            val confirmPassword = confirmPasswordEdit.text.toString()
            val nome = nomeEdit.text.toString()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || nome.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(nome)
                            .build()
                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Erro ao salvar nome: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Falha no registro: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}