package com.ud.memorygame

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private lateinit var btnLow: Button
    private lateinit var btnMedium: Button
    private lateinit var btnHard: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        addLogicToBtns()
    }

    fun addLogicToBtns(){
        btnLow = findViewById(R.id.btnLow)
        btnMedium = findViewById(R.id.btnMedium)
        btnHard = findViewById(R.id.btnHard)

        btnLow.setOnClickListener({
            Toast.makeText(this, "Clic in btn Low", Toast.LENGTH_LONG).show()
        })

        btnMedium.setOnClickListener({
            Toast.makeText(this, "Clic in btn Medium", Toast.LENGTH_LONG).show()
        })

        btnHard.setOnClickListener({
            Toast.makeText(this, "Clic in btn Hard", Toast.LENGTH_LONG).show()
        })
    }
}