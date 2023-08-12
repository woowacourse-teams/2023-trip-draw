package com.teamtripdraw.android.support.framework.presentation.loginManager

interface SocialLoginManager {
    fun startLogin(updateSocialToken: (String) -> Unit)
}
