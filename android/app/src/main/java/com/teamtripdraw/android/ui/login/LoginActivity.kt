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
        loginViewModel.checkUserHasNickName()
    }

    private fun initNickNameExistsEventObserver() {
        loginViewModel.nickNameExistsEvent.observe(this, EventObserver(this::newUserEventListener))
    }

    private fun newUserEventListener(event: Boolean) {
        if (event) {
            // todo 홈으로 이동
        } else {
            // todo 닉네임 설정뷰로 이동
        }
    }

    private fun initNewUserEventObserver() {
        loginViewModel.newUserEvent.observe(this, EventObserver(this::newUserEventListener))
    }

    private fun newUserEventListener(loginInfo: LoginInfo) {
        // todo 닉네임 설정뷰로 이동
    }

    override fun onPause() {
        loginViewModel.resetKakaoLoginEvent()
        super.onPause()
    }
}
