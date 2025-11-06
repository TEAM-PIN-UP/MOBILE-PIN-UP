package com.pinup.placePinup.domain.model


enum class PintsCategory(val kor: String) {
    ALL("전체"), Jeju("제주"), Seoul("서울"), NONE("없음");
    companion object {
        fun of(name: String): PintsCategory {
            return if (entries.any { it.name == name }) {
                PintsCategory.valueOf(name)
            } else {
                NONE
            }
        }

        fun valuesOf(kor: String): PintsCategory {
            return entries.find { it.kor == kor } ?: ALL
        }
    }
}