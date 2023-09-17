package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.enqueueActions

import com.teamtripdraw.android.TripDrawApplication.DependencyContainer.logUtil
import com.teamtripdraw.android.data.dataSource.auth.userIdentifyInfo.UserIdentifyInfoDataSource
import com.teamtripdraw.android.data.httpClient.dto.failureResponse.GeneralFailureResponse
import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.request.TokenRefreshRequest
import com.teamtripdraw.android.data.httpClient.dto.response.TokenRefreshResponse
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ServerType
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ServerType.*
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.TokenExpiryType
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.TokenExpiryType.*
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState
import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseStateCall
import com.teamtripdraw.android.data.httpClient.service.TokenRefreshService
import com.teamtripdraw.android.support.framework.data.getParsedErrorBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Retrofit

class TripDrawEnqueueActions<T : Any>(
    responseStateCall: ResponseStateCall<T>,
    retrofit: Retrofit,
    private val tokenRefreshServiceType: Class<TokenRefreshService>,
    private val userIdentifyInfoDataSource: UserIdentifyInfoDataSource.Local,
) : GeneralEnqueueActions<T>(responseStateCall, retrofit) {
    override fun responseFailureAction(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        when (ServerType.getByBaseUrl(retrofit.baseUrl().toString())) {
            NAVER_GEO_CODER -> {
                super.responseFailureAction(callback, code, errorBody)
            }
            TRIP_DRAW -> {
                tripDrawResponseFailureAction(callback, code, errorBody)
            }
        }
    }

    private fun tripDrawResponseFailureAction(
        callback: Callback<ResponseState<T>>,
        code: Int,
        errorBody: ResponseBody?,
    ) {
        if (code == TOKEN_EXPIRED_CODE) {
            when (getTokenExpiryType(errorBody)) {
                REFRESH_TOKEN -> refreshTokenExpiredAction()
                ACCESS_TOKEN -> accessTokenExpiredAction(callback)
            }
        } else {
            super.responseFailureAction(callback, code, errorBody)
        }
    }

    private fun getTokenExpiryType(errorBody: ResponseBody?): TokenExpiryType {
        val generalFailureResponse =
            retrofit.getParsedErrorBody<GeneralFailureResponse>(errorBody)
                ?: throw IllegalStateException(EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY)
        val exceptionCode = generalFailureResponse.exceptionCode
        return TokenExpiryType.getByServerExceptionCode(exceptionCode)
    }

    private fun accessTokenExpiredAction(callback: Callback<ResponseState<T>>) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching { getTokenRefreshService().tokenRefresh(getTokenRefreshRequest()) }
                .onSuccess {
                    tokenRefreshSuccessAction(it, callback)
                }
                .onFailure {
                    logUtil.general.log(it, TOKEN_REFRESH_ERROR)
                }
        }
    }

    private suspend fun tokenRefreshSuccessAction(
        tokenRefreshResponse: TokenRefreshResponse,
        callback: Callback<ResponseState<T>>,
    ) {
        userIdentifyInfoDataSource.setIdentifyInfo(tokenRefreshResponse.toData())
        val clonedResponseStateCall = responseStateCall.clone() as ResponseStateCall<T>
    }

    private fun getTokenRefreshService(): TokenRefreshService =
        retrofit.create(tokenRefreshServiceType)

    private fun getTokenRefreshRequest(): TokenRefreshRequest =
        TokenRefreshRequest(userIdentifyInfoDataSource.getRefreshToken())

    private fun refreshTokenExpiredAction() {}

    companion object {
        private const val EXPIRED_TOKEN_RESPONSE_HAS_NO_ERROR_BODY =
            "토큰 에러 관련 에러바디의 값이 null값입니다"
        private const val TOKEN_EXPIRED_CODE = 401
        private const val TOKEN_REFRESH_ERROR = "Token Refresh 과정에서 서버통신상의 문제가 생겼습니다."
    }
}
