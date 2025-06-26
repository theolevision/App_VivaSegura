package com.example.myapplication

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Google Sign-In config
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        val loginButton = findViewById<Button>(R.id.login_button)
        val googleButton = findViewById<Button>(R.id.login_google)

        val loginEdit = findViewById<EditText>(R.id.login)
        val passwordEdit = findViewById<EditText>(R.id.password)

        val irRegistro = findViewById<TextView>(R.id.ir_registro)
        irRegistro?.isClickable = true
        irRegistro?.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        // Login com email/senha
        loginButton?.setOnClickListener {
            // Aqui você pode validar o login com Firebase, mas para redirecionar direto:
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Login com Google
        val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account = task.result
                    firebaseAuthWithGoogle(account)
                } else {
                    Toast.makeText(this, "Falha no login com Google", Toast.LENGTH_SHORT).show()
                }
            }
        }

        googleButton?.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            googleSignInLauncher.launch(signInIntent)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login com Google realizado!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Falha no login com Google", Toast.LENGTH_SHORT).show()
                }
            }
    }
}