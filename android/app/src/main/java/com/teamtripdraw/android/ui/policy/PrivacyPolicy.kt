package com.teamtripdraw.android.ui.policy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teamtripdraw.android.R

class PrivacyPolicy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
    }

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, PrivacyPolicy::class.java)
            return intent
        }
    }
}
