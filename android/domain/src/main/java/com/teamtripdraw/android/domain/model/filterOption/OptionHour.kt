package com.teamtripdraw.android.domain.model.filterOption

enum class OptionHour(override val id: Int, override val value: Int) : FilterOption<Int> {
    ZERO(0, 0),
    ONE(1, 1),
    TWO(2, 2),
    THREE(3, 3),
    FOUR(4, 4),
    FIVE(5, 5),
    SIX(6, 6),
    SEVEN(7, 7),
    EIGHT(8, 8),
    NINE(9, 9),
    TEN(10, 10),
    ELEVEN(11, 11),
    TWELVE(12, 12),
    THIRTEEN(13, 13),
    FOURTEEN(14, 14),
    FIFTEEN(15, 15),
    SIXTEEN(16, 16),
    SEVENTEEN(17, 17),
    EIGHTEEN(18, 18),
    NINETEEN(19, 19),
    TWENTY(20, 20),
    TWENTY_ONE(21, 21),
    TWENTY_TWO(22, 22),
    TWENTY_THREE(23, 23),
    ;

    companion object {

        val minHour = values().minByOrNull { it.value } ?: values().first()
        val maxHour = values().maxByOrNull { it.value } ?: values().last()

        fun getSelectedHourIds(fromValue: Int, toValue: Int): List<Int> {
            val range =
                if (fromValue <= toValue) {
                    fromValue..toValue
                } else {
                    (fromValue..maxHour.value) + (minHour.value..toValue)
                }
            return range.toList()
        }
    }
}
