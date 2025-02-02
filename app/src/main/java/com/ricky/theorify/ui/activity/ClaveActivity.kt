package com.ricky.theorify.ui.activity

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import com.ricky.theorify.R

class ClaveActivity: AppCompatActivity() {
    // Declaração dos elementos que compõem a página

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clave_page)

        // Captura dos elementos que compõem a página

        // Evento "onBackPressedDispatcher" do telemóvel
        this.onBackPressedDispatcher.addCallback(this) {
        }
    }
}