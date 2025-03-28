package com.pinup.pinup.domain.model

data class DetailPlace(
    val mapPlace: ReviewedPlace,
    val ratingGraph: RatingGraph,
    val placeReviews: List<PlaceReview>
)