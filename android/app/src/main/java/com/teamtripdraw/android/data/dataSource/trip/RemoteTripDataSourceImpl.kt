package com.teamtripdraw.android.data.dataSource.trip

import com.teamtripdraw.android.data.httpClient.dto.mapper.toData
import com.teamtripdraw.android.data.httpClient.dto.mapper.toHttpRequest
import com.teamtripdraw.android.data.httpClient.service.CreateTripService
import com.teamtripdraw.android.data.httpClient.service.DeleteTripService
import com.teamtripdraw.android.data.httpClient.service.GetAllTripsService
import com.teamtripdraw.android.data.httpClient.service.GetTripInfoService
import com.teamtripdraw.android.data.httpClient.service.SetTripTitleService
import com.teamtripdraw.android.data.model.DataPreSetTripTitle
import com.teamtripdraw.android.data.model.DataPreviewTrip
import com.teamtripdraw.android.data.model.DataTrip
import com.teamtripdraw.android.data.model.DataTripOfAll
import javax.inject.Inject

class RemoteTripDataSourceImpl @Inject constructor(
    private val createTripService: CreateTripService,
    private val getTripInfoService: GetTripInfoService,
    private val setTripTitleService: SetTripTitleService,
    private val getAllTripsService: GetAllTripsService,
    private val deleteTripService: DeleteTripService,
) :
    TripDataSource.Remote {
    override suspend fun startTrip(): Result<Long> =
        createTripService.startTrip().process { body, _ ->
            Result.success(body.tripId)
        }

    override suspend fun getTripInfo(tripId: Long): Result<DataTrip> =
        getTripInfoService.getTripInfo(tripId).process { body, _ ->
            Result.success(body)
        }.map { it.toData() }

    override suspend fun setTripTitle(
        tripId: Long,
        dataPreSetTripTitle: DataPreSetTripTitle,
    ): Result<Unit> =
        setTripTitleService.setTripTitle(tripId, dataPreSetTripTitle.toHttpRequest())
            .process { body, _ ->
                Result.success(body)
            }

    override suspend fun getMyTrips(): Result<List<DataPreviewTrip>> =
        getAllTripsService.getMyTrips().process { body, _ ->
            Result.success(body.toData())
        }

    override suspend fun getAllTrips(
        address: String,
        ageRanges: List<Int>,
        daysOfWeek: List<Int>,
        genders: List<Int>,
        months: List<Int>,
        years: List<Int>,
        lastViewedId: Long?,
        limit: Int,
    ): Result<List<DataTripOfAll>> {
        return getAllTripsService.getAllTrips(
            years = years,
            months = months,
            daysOfWeek = daysOfWeek,
            ageRanges = ageRanges,
            genders = genders,
            address = address,
            lastViewedId = lastViewedId,
            limit = limit,
        ).process { body, headers ->
            Result.success(body.toData())
        }
    }

    override suspend fun deleteTrip(tripId: Long): Result<Unit> =
        deleteTripService.deleteTrip(tripId).process { body, _ ->
            Result.success(body)
        }
}
