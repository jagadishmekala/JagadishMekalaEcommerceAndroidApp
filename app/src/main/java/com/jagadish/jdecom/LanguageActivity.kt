package com.jagadish.jdecom


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.widget.Button
import androidx.core.content.ContextCompat
import java.util.*

class LanguageActivity : AppCompatActivity() {

    private lateinit var cardEnglish: CardView
    private lateinit var cardTelugu: CardView
    private lateinit var btnContinue: Button
    private var selectedLang = "en"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)
        window.statusBarColor = ContextCompat.getColor(this, R.color.blue_dark)
        cardEnglish = findViewById(R.id.cardEnglish)
        cardTelugu = findViewById(R.id.cardTelugu)
        btnContinue = findViewById(R.id.btnContinue)

        // Default English
        setLocale("en")

        cardEnglish.setOnClickListener {
            selectedLang = "en"
            setLocale("en")
        }

        cardTelugu.setOnClickListener {
            selectedLang = "te"
            setLocale("te")
        }

        btnContinue.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        resources.updateConfiguration(config, resources.displayMetrics)

        // Save preference
        val prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        prefs.edit().putString("Language", languageCode).apply()

        // Update title instantly
        val titleView = findViewById<android.widget.TextView>(R.id.tvChooseLanguage)
        titleView.text = if (languageCode == "te") "మీ భాషను ఎంచుకోండి" else "Choose Your Language"
    }
}
