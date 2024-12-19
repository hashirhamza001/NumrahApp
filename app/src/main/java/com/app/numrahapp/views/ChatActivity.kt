package com.app.numrahapp.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.numrahapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
      
    }
}