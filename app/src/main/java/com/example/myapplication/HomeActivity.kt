package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().load(applicationContext, getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.home)

        val btnPanico = findViewById<Button>(R.id.panico)
        btnPanico.setOnClickListener {
            val intent = Intent(this, PanicActivity::class.java)
            startActivity(intent)
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val btnDelegacia = findViewById<Button>(R.id.btn_delegacia)
        btnDelegacia.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    val lat = location?.latitude ?: -14.2350
                    val lon = location?.longitude ?: -51.9253
                    val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=Delegacia da Mulher")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    if (mapIntent.resolveActivity(packageManager) != null) {
                        startActivity(mapIntent)
                    } else {
                        val browserIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        startActivity(browserIntent)
                    }
                }
            }
        }

        mapView = findViewById(R.id.mapView)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(4.0)
        mapView.controller.setCenter(GeoPoint(-14.2350, -51.9253))
        val user = FirebaseAuth.getInstance().currentUser
        val nome = user?.displayName ?: "Usuário"
        val olaTextView = findViewById<TextView>(R.id.textView2) // Troque pelo id correto do seu TextView
        olaTextView.text = "Olá $nome"
        val evidenciasButton = findViewById<Button>(R.id.evidenciasButton) // Troque pelo id do seu botão
        evidenciasButton.setOnClickListener {
            val intent = Intent(this, EvidenciasActivity::class.java)
            startActivity(intent)
        }
        val anotacoesButton = findViewById<Button>(R.id.anotacoes)
        anotacoesButton.setOnClickListener {
            val intent = Intent(this, AnotacoesActivity::class.java)
            startActivity(intent)
        }
    }
}