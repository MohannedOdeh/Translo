package com.example.translationprojectfinal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun move(v: View)
    {
        val intent = Intent(this, IdentTranslat::class.java)
        startActivity(intent)
    }

    fun move2(v: View)
    {
        val intent = Intent(this, TranslationActivity::class.java)
        startActivity(intent)
    }

    fun move3(v: View)
    {
        val intent = Intent(this, Camera::class.java)
        startActivity(intent)
    }
}