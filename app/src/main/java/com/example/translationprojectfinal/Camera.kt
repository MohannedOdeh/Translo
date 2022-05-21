package com.example.translationprojectfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Camera : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CameraSet.newInstance())
                .commitNow()
        }
    }

}
