package com.teamtripdraw.android.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teamtripdraw.android.databinding.ActivityOpenSourceLicenseBinding

class OpenSourceLicenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOpenSourceLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSourceLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBackClickEvent()
    }

    private fun initBackClickEvent() {
        binding.btnOpenSourceLicenseBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, OpenSourceLicenseActivity::class.java)
            return intent
        }
    }
}
