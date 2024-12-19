package com.app.numrahapp.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.app.numrahapp.R
import com.app.numrahapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var isGenderSelected: Boolean=false
    lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvContinue.setOnClickListener {
            if (binding.etUsername.text.isEmpty()) {
                Toast.makeText(this, getString(R.string.username_is_required),Toast.LENGTH_SHORT).show()
            }else if (!isGenderSelected) {
                Toast.makeText(this, getString(R.string.select_a_gender),Toast.LENGTH_SHORT).show()
            }else{
                startActivity(Intent(this, ChatActivity::class.java))
                finish()
            }

        }
        binding.ivMale.setOnClickListener {
            selectGender(it)
        }
        binding.ivFemale.setOnClickListener {
            selectGender(it)
        }
    }

    private fun selectGender(it: View) {
        binding.ivMale.background = null
        binding.ivFemale.background = null
        it.setBackgroundResource(R.drawable.bg_circle)
        isGenderSelected=true
    }
}