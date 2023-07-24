package com.teamtripdraw.android.ui.signUp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityNicknameSetupBinding
import com.teamtripdraw.android.domain.user.NicknameValidState
import com.teamtripdraw.android.ui.common.EventObserver
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

        setupDuplicateNicknameDialog()
        setupNicknameCompleteButton()
        setupNavigation()
    }

    private fun setupDuplicateNicknameDialog() {
        viewModel.nicknameState.observe(this) {
            showDuplicateNicknameDialog(it)
        }
    }

    private fun showDuplicateNicknameDialog(it: NicknameValidState?) {
        if (it == NicknameValidState.DUPLICATE) {
            Toast.makeText(this, R.string.duplicate_nickname, Toast.LENGTH_SHORT).show()
        }
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
