package com.teamtripdraw.android.support.framework.presentation.loginManager

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLoginManager(
    private val context: Context,
) : SocialLoginManager {
    private lateinit var kakaoLoginState: KaKaoLoginState
    private lateinit var kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit

    override fun startLogin(
        updateSocialToken: (String) -> Unit,
    ) {
        kakaoLoginState = getKaKaoLoginState()
        kakaoLoginCallback = getLoginCallback(updateSocialToken)

        when (kakaoLoginState) {
            KaKaoLoginState.KAKAO_TALK_LOGIN -> onKakaoTalkLogin(updateSocialToken)
            KaKaoLoginState.KAKAO_ACCOUNT_LOGIN -> onKakaoAccountLogin()
        }
    }

    private fun getKaKaoLoginState(): KaKaoLoginState =
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            KaKaoLoginState.KAKAO_TALK_LOGIN
        } else {
            KaKaoLoginState.KAKAO_ACCOUNT_LOGIN
        }

    private fun getLoginCallback(updateSocialToken: (String) -> Unit): (OAuthToken?, Throwable?) -> Unit {
        return { token, error ->
            if (error != null) {
                Log.d(this.javaClass.name, "${error.message} 카카오 계정으로 로그인 실패")
            } else if (token != null) {
                updateSocialToken(token.accessToken)
            }
        }
    }

    private fun onKakaoTalkLogin(updateSocialToken: (String) -> Unit) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                // 카카오톡으로 로그인 실패
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }
                onKakaoAccountLogin()
            } else if (token != null) {
                updateSocialToken(token.accessToken)
            }
        }
    }

    private fun onKakaoAccountLogin() {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = kakaoLoginCallback)
    }

    enum class KaKaoLoginState {
        KAKAO_TALK_LOGIN, KAKAO_ACCOUNT_LOGIN
    }
}
