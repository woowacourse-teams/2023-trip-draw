package com.teamtripdraw.android.testDouble

import com.teamtripdraw.android.domain.model.point.Route
import com.teamtripdraw.android.domain.model.trip.PreSetTripTitle
import com.teamtripdraw.android.domain.model.trip.PreviewTrip
import com.teamtripdraw.android.domain.model.trip.Trip
import com.teamtripdraw.android.domain.repository.TripRepository

class FakeTripRepository : TripRepository {
    val trips: MutableList<Trip> = mutableListOf()

    override suspend fun startTrip(): Result<Unit> {
        // todo 구현
        return Result.success(Unit)
    }

    override fun getCurrentTripId(): Long {
        return trips.lastIndex.toLong()
    }

    override fun deleteCurrentTripId() {
        trips.removeLast()
    }

    override suspend fun getCurrentTripRoute(tripId: Long): Result<Route> {
        val trip = trips.find { it.tripId == tripId }
        return if (trip != null) {
            Result.success(trip.route)
        } else {
            Result.failure(NullPointerException())
        }
    }

    override suspend fun getTrip(tripId: Long): Result<Trip> {
        val trip = trips.find { it.tripId == tripId }
        return if (trip != null) {
            Result.success(trip)
        } else {
            Result.failure(NullPointerException())
        }
    }

    override suspend fun setTripTitle(
        tripId: Long,
        preSetTripTitle: PreSetTripTitle,
    ): Result<Unit> {
        val beforeTrip = trips.find { it.tripId == tripId }
            ?: return Result.failure(NullPointerException())

        val afterTrip = Trip(
            tripId = tripId,
            name = preSetTripTitle.name,
            route = beforeTrip.route,
            status = preSetTripTitle.status.name,
            imageUrl = beforeTrip.imageUrl,
            routeImageUrl = beforeTrip.imageUrl,
        )

        trips.removeIf { it.tripId == beforeTrip.tripId }
        trips.add(afterTrip)

        return Result.success(Unit)
    }

    override suspend fun getAllTrips(): Result<List<PreviewTrip>> {
        return Result.success(
            trips.map {
                PreviewTrip(
                    id = it.tripId,
                    name = it.name,
                    imageUrl = it.imageUrl ?: "",
                    routeImageUrl = it.routeImageUrl ?: "",
                )
            },
        )
    }

    override suspend fun deleteTrip(tripId: Long): Result<Unit> {
        trips.removeIf { it.tripId == tripId }
        return Result.success(Unit)
    }
}
