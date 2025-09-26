package com.pinup.pinup.domain.model

data class DetailPlace(
    val mapPlace: ReviewedPlace = ReviewedPlace(),
    val ratingGraph: RatingGraph = RatingGraph(),
    val placeReviews: List<PlaceReview> = emptyList()
)