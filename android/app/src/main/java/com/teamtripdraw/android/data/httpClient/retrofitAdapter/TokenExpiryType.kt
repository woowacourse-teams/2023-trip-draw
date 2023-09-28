package com.teamtripdraw.android.data.httpClient.retrofitAdapter

enum class TokenExpiryType(private val serverExceptionCode: String) {
    ACCESS_TOKEN("EXPIRED_ACCESS_TOKEN"),
    REFRESH_TOKEN("EXPIRED_REFRESH_TOKEN"),
    ;

    companion object {
        private const val UNKNOWN_TOKEN_TYPE_ERROR = """
        입력하신 exceptionCode에 일치하는 Token type가 정의 되어있지 않습니다.
        서버 명세 혹은 TokenExpiryType을 확인하세요(현재 입력된 serverExceptionCode: %s)
        """

        fun getByServerExceptionCode(exceptionCode: String): TokenExpiryType =
            values().find {
                it.serverExceptionCode == exceptionCode
            } ?: throw IllegalArgumentException(UNKNOWN_TOKEN_TYPE_ERROR.format(exceptionCode))
    }
}
