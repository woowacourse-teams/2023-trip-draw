package com.teamtripdraw.android.domain.model.filterOption

sealed interface FilterOption {
    val id: Int
    val value: Any
}
