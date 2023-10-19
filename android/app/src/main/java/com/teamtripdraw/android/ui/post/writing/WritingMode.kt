package com.teamtripdraw.android.ui.post.writing

import com.teamtripdraw.android.domain.model.point.Point
import com.teamtripdraw.android.domain.model.post.Post
import com.teamtripdraw.android.domain.model.trip.Trip

enum class WritingMode {
    NEW_RECORDED_POINT,
    NEW_CURRENT_POINT,
    EDIT,
    ;

    companion object {
        fun getWritingMode(
            tripId: Long,
            pointId: Long,
            postId: Long,
            latitude: Double,
            longitude: Double,
        ): WritingMode {
            return if (tripId != Trip.NULL_SUBSTITUTE_ID && pointId != Point.NULL_SUBSTITUTE_ID) {
                NEW_RECORDED_POINT
            } else if (postId != Post.NULL_SUBSTITUTE_ID) {
                EDIT
            } else if (latitude != Point.NULL_LATITUDE && longitude != Point.NULL_LONGITUDE) {
                NEW_CURRENT_POINT
            } else {
                throw IllegalArgumentException("WritingMode를 결정할 수 없습니다. 필요한 값을 모두 설정하였는지 확인해주세요")
            }
        }
    }
}
