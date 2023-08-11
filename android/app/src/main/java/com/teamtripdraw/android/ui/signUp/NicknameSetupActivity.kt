package com.teamtripdraw.android.ui.signUp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityNicknameSetupBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.main.MainActivity
import com.teamtripdraw.android.ui.model.UiLoginInfo

class NicknameSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNicknameSetupBinding
    private val viewModel: NicknameSetupViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nickname_setup)

        binding.lifecycleOwner = this
        binding.nicknameSetupViewModel = viewModel

        setupNavigation()
    }

    private fun setupNavigation() {
        viewModel.nicknameSetupCompleteEvent.observe(
            this,
            EventObserver(this@NicknameSetupActivity::navigateNextPage),
        )
    }

    private fun navigateNextPage(isNicknameSetupCompleted: Boolean) {
        if (isNicknameSetupCompleted) {
            startActivity(MainActivity.getIntent(this))
            finish()
        }
    }

    companion object {
        private const val UI_LOGIN_INFO = "UI_LOGIN_INFO"

        fun getIntent(context: Context, uiLoginInfo: UiLoginInfo): Intent =
            Intent(context, NicknameSetupActivity::class.java).apply {
                putExtra(UI_LOGIN_INFO, uiLoginInfo)
            }
    }
}
