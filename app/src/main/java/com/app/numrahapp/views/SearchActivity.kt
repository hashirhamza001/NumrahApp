package com.app.numrahapp.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.numrahapp.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    lateinit var binding: ActivitySearchBinding
    var selectedGender=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        selectedGender= intent.getStringExtra("gender").toString()
        binding.circularProgressIndicator.isIndeterminate=true
    }
}