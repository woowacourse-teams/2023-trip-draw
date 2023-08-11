package com.teamtripdraw.android.data.dataSource.signUp

import com.teamtripdraw.android.data.httpClient.dto.failureResponse.NicknameSetupFailureResponse
import com.teamtripdraw.android.data.httpClient.dto.request.NicknameSetUpRequest
import com.teamtripdraw.android.data.httpClient.service.GetUserInfoService
import com.teamtripdraw.android.data.httpClient.service.NicknameSetupService
import com.teamtripdraw.android.data.model.DataLoginInfo
import com.teamtripdraw.android.data.model.DataUserInfo
import com.teamtripdraw.android.domain.exception.DuplicateNicknameException
import com.teamtripdraw.android.domain.exception.InvalidNicknameException
import com.teamtripdraw.android.support.framework.data.getParsedErrorBody
import okhttp3.ResponseBody
import retrofit2.Retrofit

class RemoteSignUpDataSourceImpl(
    private val nicknameSetupService: NicknameSetupService,
    private val getUserInfoService: GetUserInfoService,
    private val retrofit: Retrofit,
) :
    SignUpDataSource.Remote {
    override suspend fun setNickname(
        nickname: String,
        dataLoginInfo: DataLoginInfo,
    ): Result<String> =
        nicknameSetupService.setNickname(getNicknameSetUpRequest(nickname, dataLoginInfo))
            .process(failureListener = this::setNicknameFailureListener) { body, headers ->
                Result.success(body.accessToken)
            }

    private fun getNicknameSetUpRequest(
        nickname: String,
        dataLoginInfo: DataLoginInfo,
    ): NicknameSetUpRequest =
        NicknameSetUpRequest(
            nickname = nickname,
            oauthToken = dataLoginInfo.socialToken,
            oauthType = dataLoginInfo.platform,
        )

    private fun setNicknameFailureListener(code: Int, errorBody: ResponseBody?): Result<Nothing> {
        if (code == 409) {
            val message =
                retrofit.getParsedErrorBody<NicknameSetupFailureResponse>(errorBody)?.message
            return Result.failure(
                DuplicateNicknameException(
                    message ?: DEFAULT_DUPLICATE_NICKNAME_EXCEPTION_MESSAGE,
                ),
            )
        }
        // 중복 닉네임을 제외한 오류들은 code400으로 분기되어있다.(#43 참고)
        return Result.failure(
            InvalidNicknameException(
                DEFAULT_INVALID_NICKNAME_EXCEPTION_MESSAGE,
            ),
        )
    }

    override suspend fun getUserInfo(accessToken: String): Result<DataUserInfo> =
        getUserInfoService.getUserInfo(accessToken).process { body, headers ->
            Result.success(DataUserInfo(memberId = body.memberId, nickname = body.nickname))
        }

    companion object {
        private const val DEFAULT_DUPLICATE_NICKNAME_EXCEPTION_MESSAGE = "중복된 닉네임 입니다."
        private const val DEFAULT_INVALID_NICKNAME_EXCEPTION_MESSAGE =
            "공백 문자 혹은 10자를 초과한 부적절한 닉네임 입니다."
    }
}
