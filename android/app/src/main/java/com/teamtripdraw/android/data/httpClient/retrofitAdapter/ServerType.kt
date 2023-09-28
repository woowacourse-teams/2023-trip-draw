package com.teamtripdraw.android.data.httpClient.retrofitAdapter

import com.teamtripdraw.android.BuildConfig

enum class ServerType(private val baseURl: String) {
    TRIP_DRAW(BuildConfig.TRIP_DRAW_BASE_URL),
    NAVER_REVERSE_GEOCODER(BuildConfig.NAVER_REVERS_GEOCODER_BASE_URL),
    ;

    companion object {
        private const val UNKOWN_BASE_URL_ERROR = """
        입력하신 BaseURl에 일치하는 서버 Type가 정의되어 있지않습니다.
        정의되어있는 BaseURL을 확인하세요.
        """

        fun getByBaseUrl(baseURL: String): ServerType =
            values().find {
                it.baseURl == baseURL
            } ?: throw IllegalArgumentException(UNKOWN_BASE_URL_ERROR)
    }
}
