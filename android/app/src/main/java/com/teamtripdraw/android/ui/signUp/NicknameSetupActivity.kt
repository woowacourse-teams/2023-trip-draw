package com.teamtripdraw.android.ui.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.teamtripdraw.android.databinding.ActivityNicknameSetupBinding

class NicknameSetupActivity : AppCompatActivity() {

    private val binding: ActivityNicknameSetupBinding by lazy {
        ActivityNicknameSetupBinding.inflate(layoutInflater)
    }
    private val viewModel: NicknameSetupViewModel by lazy {
        ViewModelProvider(this)[NicknameSetupViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.nicknameSetupViewModel = viewModel

        viewModel.nickname.observe(this) {
            viewModel.updateNickNameState(viewModel.nickname.value!!)
        }
    }
}
