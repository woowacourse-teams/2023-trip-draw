package com.teamtripdraw.android.ui.signUp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityNicknameSetupBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NicknameSetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNicknameSetupBinding
    private val viewModel: NicknameSetupViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nickname_setup)

        binding.lifecycleOwner = this
        binding.nicknameSetupViewModel = viewModel

        setupNicknameCompleteButton()
        setupNavigation()
    }

    private fun setupNicknameCompleteButton() {
        binding.btnSetupComplete.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.onNicknameSetupComplete()
            }
        }
    }

    private fun setupNavigation() {
        viewModel.nicknameSetupCompleteEvent.observe(
            this, EventObserver(this@NicknameSetupActivity::navigateNextPage)
        )
    }

    private fun navigateNextPage(isNicknameSetupCompleted: Boolean) {
        if (isNicknameSetupCompleted) {
            MainActivity.startActivity(this)
        } else {
            Toast.makeText(this, UNKNOWN_EXCEPTION, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val UNKNOWN_EXCEPTION = "UnknownException"
    }
}
