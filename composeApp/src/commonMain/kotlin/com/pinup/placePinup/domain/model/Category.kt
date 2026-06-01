package com.pinup.placePinup.domain.model

enum class CategoryGroup {
    FNB,
    NATURE,
    CULTURE,
    ETC,
}

enum class Category(
    val displayName: String,
    val code: String,
    val group: CategoryGroup,
) {
    ALL("전체", "ALL", CategoryGroup.ETC),
    RESTAURANT("음식점", "FD6", CategoryGroup.FNB),
    CAFE("카페", "CE7", CategoryGroup.FNB),
    MART("대형마트", "MT1", CategoryGroup.FNB),
    STORE("편의점", "CS2", CategoryGroup.FNB),
    KINDERGARTEN("어린이집, 유치원", "PS3", CategoryGroup.CULTURE),
    SCHOOL("학교", "SC4", CategoryGroup.CULTURE),
    ACADEMY("학원", "AC5", CategoryGroup.CULTURE),
    PARKING_LOT("주차장", "PK6", CategoryGroup.ETC),
    GAS_STATION("주유소, 충전소", "OL7", CategoryGroup.ETC),
    SUBWAY("지하철역", "SW8", CategoryGroup.ETC),
    BANK("은행", "BK9", CategoryGroup.ETC),
    CULTURAL_FACILITY("문화시설", "CT1", CategoryGroup.CULTURE),
    BROKERAGE("중개업소", "AG2", CategoryGroup.ETC),
    PUBLIC_OFFICE("공공기관", "PO3", CategoryGroup.ETC),
    ATTRACTION("관광명소", "AT4", CategoryGroup.NATURE),
    ACCOMMODATION("숙박", "AD5", CategoryGroup.CULTURE),
    HOSPITAL("병원", "HP8", CategoryGroup.ETC),
    PHARMACY("약국", "PM9", CategoryGroup.ETC),
    ETC("기타시설", "ETC", CategoryGroup.ETC),
    NONE("", "", CategoryGroup.ETC);

    companion object {
        fun of(value: String): Category {
            return entries.firstOrNull { it.name == value || it.code == value } ?: NONE
        }
    }
}
