package com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.responseStateCallExtraFeature

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.responseState.ResponseState

// todo Kapt를 통해 외부 입력받을 수 있도록 수정
interface EnqueueSuspend<T : Any> {
    suspend fun enqueueSuspend(): ResponseState<T>
}
