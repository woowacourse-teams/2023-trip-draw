package com.teamtripdraw.android.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy)

        initBackClickEvent()
    }

    private fun initBackClickEvent() {
        binding.btnPrivacyPolicyBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, PrivacyPolicyActivity::class.java)
            return intent
        }
    }
}
