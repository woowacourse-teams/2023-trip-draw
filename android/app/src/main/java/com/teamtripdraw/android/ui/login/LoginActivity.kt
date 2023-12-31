package com.teamtripdraw.android.ui.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.teamtripdraw.android.BuildConfig
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityLoginBinding
import com.teamtripdraw.android.domain.model.auth.LoginInfo
import com.teamtripdraw.android.domain.model.auth.LoginPlatform
import com.teamtripdraw.android.support.framework.presentation.event.EventObserver
import com.teamtripdraw.android.support.framework.presentation.loginManager.KakaoLoginManager
import com.teamtripdraw.android.support.framework.presentation.loginManager.SocialLoginManager
import com.teamtripdraw.android.ui.main.MainActivity
import com.teamtripdraw.android.ui.model.mapper.toPresentation
import com.teamtripdraw.android.ui.signUp.NicknameSetupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    private lateinit var kakaoLoginManager: SocialLoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        autoLogin()
        initObserver()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.loginViewModel = loginViewModel
        initSocialLoginMangers()
    }

    private fun autoLogin() {
        loginViewModel.fetchAutoLoginState()
    }

    private fun initAutoLoginEventObserver() {
        loginViewModel.autoLoginEvent.observe(this, EventObserver(this::autoLoginEventListener))
    }

    private fun autoLoginEventListener(event: Boolean) {
        if (event) {
            loginViewModel.fetchUserHasNickname()
        }
    }

    private fun initObserver() {
        initKaKaoLoginEventObserver()
        initOpenPrivacyPolicyEventObserver()
        initOpenTermsOfServiceEventObserver()
        initExistedUserEventObserver()
        initNewUserEventObserver()
        initNicknameExistsEventObserver()
        initAutoLoginEventObserver()
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

    private fun initOpenPrivacyPolicyEventObserver() {
        loginViewModel.openPrivacyPolicyEvent.observe(this) {
            if (it.not()) return@observe
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_POLICY_URL))
            startActivity(browserIntent)
        }
    }

    private fun initOpenTermsOfServiceEventObserver() {
        loginViewModel.openTermsOfServiceEvent.observe(this) {
            if (it.not()) return@observe
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.TERMS_OF_SERVICE_URL))
            startActivity(browserIntent)
        }
    }

    private fun initExistedUserEventObserver() {
        loginViewModel.existedUserEvent.observe(this, EventObserver(this::existedUserEventListener))
    }

    private fun existedUserEventListener(loginInfo: LoginInfo) {
        loginViewModel.fetchUserHasNickname()
    }

    private fun initNicknameExistsEventObserver() {
        loginViewModel.nicknameExistsEvent.observe(this, EventObserver(this::newUserEventListener))
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
        finish()
    }

    private fun moveToNicknameSetupActivity(loginInfo: LoginInfo) {
        val intent = NicknameSetupActivity.getIntent(this, loginInfo.toPresentation())
        startActivity(intent)
        finish()
    }

    override fun onPause() {
        loginViewModel.resetKakaoLoginEvent()
        super.onPause()
    }

    companion object {
        fun getIntent(context: Context, vararg flags: Int): Intent =
            Intent(context, LoginActivity::class.java).apply {
                if (flags.isNotEmpty()) {
                    val combinedFlags = flags.reduce { acc, flag -> acc or flag }
                    this.flags = combinedFlags
                }
            }
    }
}
