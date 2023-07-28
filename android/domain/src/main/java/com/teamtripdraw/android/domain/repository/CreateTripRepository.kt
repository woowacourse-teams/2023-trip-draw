package com.teamtripdraw.android.domain.repository

interface CreateTripRepository {
    suspend fun startTrip(): Result<Long>
}
