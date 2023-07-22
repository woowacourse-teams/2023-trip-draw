package com.teamtripdraw.android.data.repository

import com.teamtripdraw.android.data.dataSource.nicknameSetup.NicknameSetupDataSource
import com.teamtripdraw.android.data.httpClient.dto.failureResponse.NicknameSetupFailureReponse
import com.teamtripdraw.android.domain.exception.nicknameSetup.DuplicateNickNameException
import com.teamtripdraw.android.domain.exception.nicknameSetup.InvalidNickNameException
import com.teamtripdraw.android.domain.repository.NicknameSetupRepository
import com.teamtripdraw.android.support.framework.data.getParsedErrorBody
import okhttp3.ResponseBody
import retrofit2.Retrofit

class NicknameSetupRepositoryImpl(
    private val nicknameSetupDataSource: NicknameSetupDataSource.Remote,
    private val retrofit: Retrofit
) :
    NicknameSetupRepository {
    override suspend fun setNickName(nickName: String): Result<Unit> {
        return nicknameSetupDataSource.setNickName(nickName)
            .process(failureListener = this::setNickNameFailureListener) { body, headers ->
                Result.success(Unit)
                // todo local property를 세팅하여 닉네임 값을 저장하고 인터셉터에 붙이는 로직 작성 (#59)
            }
    }

    private fun setNickNameFailureListener(code: Int, errorBody: ResponseBody?): Result<Nothing> {
        if (code == 409) {
            val message = retrofit.getParsedErrorBody<NicknameSetupFailureReponse>(errorBody)?.message
            return Result.failure(
                DuplicateNickNameException(
                    message ?: DEFAULT_DUPLICATE_NICKNAME_EXCEPTION_MESSAGE
                )
            )
        }
        // 중복 닉네임을 제외한 오류들은 code400으로 분기되어있다.(#43 참고)
        return Result.failure(InvalidNickNameException(DEFAULT_INVALID_NICKNAME_EXCEPTION_MESSAGE))
    }

    companion object {
        private const val DEFAULT_DUPLICATE_NICKNAME_EXCEPTION_MESSAGE = "중복된 닉네임 입니다."
        private const val DEFAULT_INVALID_NICKNAME_EXCEPTION_MESSAGE =
            "공백 문자 혹은 10자를 초과한 부적절한 닉네임 입니다."
    }
}
