package com.app.numrahapp.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.numrahapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        const val  MALE_TAG="MALE"
        const val  FEMALE_TAG="FEMALE"
        const val  EVERYONE_TAG="EVERYONE"
    }
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ivMale.setOnClickListener {
            goToSearchScreen(MALE_TAG)
        }
        binding.ivFemale.setOnClickListener {
            goToSearchScreen(FEMALE_TAG)
        }
        binding.ivEveryone.setOnClickListener {
            goToSearchScreen(EVERYONE_TAG)
        }
    }

    private fun goToSearchScreen(tag: String) {
        startActivity(Intent(this, SearchActivity::class.java).putExtra("gender",tag))
    }
}