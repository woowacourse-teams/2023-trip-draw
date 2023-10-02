package com.teamtripdraw.android.domain.model.filterOption

enum class OptionYear(override val id: Int, override val value: Any) : FilterOption {
    TWO_THOUSAND_TWENTY(id = 2020, value = 2020),
    TWO_THOUSAND_TWENTY_ONE(id = 2021, value = 2021),
    TWO_THOUSAND_TWENTY_TWO(id = 2022, value = 2022),
    TWO_THOUSAND_TWENTY_THREE(id = 2023, value = 2023),
}
