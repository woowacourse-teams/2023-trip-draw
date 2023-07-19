package com.teamtripdraw.android.ui.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityNicknameSetupBinding

class NicknameSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNicknameSetupBinding
    private val viewModel: NicknameSetupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nickname_setup)

        binding.lifecycleOwner = this
        binding.nicknameSetupViewModel = viewModel

        viewModel.nickname.observe(this) {
            viewModel.updateNickNameState(it)
        }
    }
}
