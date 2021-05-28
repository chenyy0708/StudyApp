package com.example.study

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.study.databinding.ActivityMainBinding
import com.example.study.demo.RVActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)
        }
    }

    fun recyclerView(view: View) {
        startActivity(Intent(this,RVActivity::class.java))
    }
}