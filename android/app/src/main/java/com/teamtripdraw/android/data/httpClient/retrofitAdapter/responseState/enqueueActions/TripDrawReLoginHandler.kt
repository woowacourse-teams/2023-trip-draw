package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TripDrawReLoginHandler(
    private val context: Context,
    private val userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
) : ReLoginHandler {
    // todo 사용자 편의를 위한 추가작업 필요 #352 참고
    override fun reLogin() {
        userIdentifyInfoDataSource.deleteIdentifyInfo()
        context.startActivity(
            LoginActivity.getIntent(
                context,
                Intent.FLAG_ACTIVITY_CLEAR_TASK,
                Intent.FLAG_ACTIVITY_NEW_TASK,
            ),
        )
        notifyReLoginMessage()
    }

    private fun notifyReLoginMessage() {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, TOKEN_EXPIRED_MESSAGE, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TOKEN_EXPIRED_MESSAGE = "로그인이 만료되었습니다.\n재로그인이 필요합니다."
    }
}
