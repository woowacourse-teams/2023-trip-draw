package com.teamtripdraw.android.ui.myPage.accountDeletion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityAccountDeletionBinding
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory

class AccountDeletionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountDeletionBinding
    private val viewModel: AccountDeletionViewModel by viewModels { tripDrawViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_deletion)
        binding.lifecycleOwner = this
        binding.accountDeletionViewModel = viewModel

        initBackPageObserve()
        initAccountDeletionCompleteObserve()
    }

    private fun initBackPageObserve() {
        viewModel.backEvent.observe(
            this,
            EventObserver { if (it) finish() },
        )
    }

    private fun initAccountDeletionCompleteObserve() {
        viewModel.accountDeletionCompleteEvent.observe(
            this,
            EventObserver {
                // todo 로그인 화면으로 가는 로직
                if (it) finish()
            },
        )
    }

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, AccountDeletionActivity::class.java)
            return intent
        }
    }
}
