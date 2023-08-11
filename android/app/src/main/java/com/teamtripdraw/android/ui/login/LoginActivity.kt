package com.teamtripdraw.android.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityLoginBinding
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginPlatform
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.loginManager.KakaoLoginManager
import com.teamtripdraw.android.support.framework.presentation.loginManager.SocialLoginManager
import com.teamtripdraw.android.ui.common.tripDrawViewModelFactory
import com.teamtripdraw.android.ui.main.MainActivity
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import com.teamtripdraw.android.ui.signUp.NicknameSetupActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels { tripDrawViewModelFactory }

    private lateinit var kakaoLoginManager: SocialLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
        initSocialLoginMangers()
        initObserver()
    }

    private fun initObserver() {
        initKaKaoLoginEventObserver()
        initExistedUserEventObserver()
        initNewUserEventObserver()
        initNickNameExistsEventObserver()
    }

    private fun initSocialLoginMangers() {
        kakaoLoginManager = KakaoLoginManager(this)
    }

    private fun initKaKaoLoginEventObserver() {
        loginViewModel.kakaoLoginEvent.observe(this, this::kaKaoLoginEventListener)
    }

    private fun kaKaoLoginEventListener(event: Boolean) {
        if (event) {
            kakaoLoginManager.startLogin { socialToken ->
                loginViewModel.login(LoginPlatform.KAKAO, socialToken)
            }
        }
    }

    private fun initExistedUserEventObserver() {
        loginViewModel.existedUserEvent.observe(this, EventObserver(this::existedUserEventListener))
    }

    private fun existedUserEventListener(loginInfo: LoginInfo) {
        loginViewModel.fetchUserHasNickName()
    }

    private fun initNickNameExistsEventObserver() {
        loginViewModel.nickNameExistsEvent.observe(this, EventObserver(this::newUserEventListener))
    }

    private fun newUserEventListener(event: Boolean) {
        if (event) {
            moveToMainActivity()
        } else {
            loginViewModel.existedUserEventValue?.let { loginInfo ->
                moveToNicknameSetupActivity(loginInfo)
            }
        }
    }

    private fun initNewUserEventObserver() {
        loginViewModel.newUserEvent.observe(this, EventObserver(this::newUserEventListener))
    }

    private fun newUserEventListener(loginInfo: LoginInfo) {
        moveToNicknameSetupActivity(loginInfo)
    }

    private fun moveToMainActivity() {
        val intent = MainActivity.getIntent(this)
        startActivity(intent)
    }

    private fun moveToNicknameSetupActivity(loginInfo: LoginInfo) {
        val intent = NicknameSetupActivity.getIntent(this, loginInfo.toPresentation())
        startActivity(intent)
    }

    override fun onPause() {
        loginViewModel.resetKakaoLoginEvent()
        super.onPause()
    }
}
