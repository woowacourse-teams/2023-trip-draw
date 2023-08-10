package com.teamtripdraw.android.data.httpClient.service

import com.teamtripdraw.android.data.httpClient.retrofitAdapter.ResponseState
import retrofit2.http.DELETE
import retrofit2.http.Path

interface DeleteTripService {
    @DELETE("/trips/{tripId}")
    suspend fun deleteTrip(@Path("tripId") tripId: Long): ResponseState<Unit>
}
