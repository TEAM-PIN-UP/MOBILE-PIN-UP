package com.pinup.placePinup.domain.model

import org.jetbrains.compose.resources.StringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.sort_latest
import pinup.composeapp.generated.resources.sort_near
import pinup.composeapp.generated.resources.sort_star_high
import pinup.composeapp.generated.resources.sort_star_low

enum class SortType(val textRes: StringResource) {
    NEAR(Res.string.sort_near),
    LATEST(Res.string.sort_latest),
    STAR_HIGH(Res.string.sort_star_high),
    STAR_LOW(Res.string.sort_star_low)
}
