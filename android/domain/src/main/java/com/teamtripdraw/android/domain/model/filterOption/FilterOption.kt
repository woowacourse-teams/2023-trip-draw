package com.teamtripdraw.android.domain.model.filterOption

sealed interface FilterOption<T> {
    val id: Int
    val value: T
}
