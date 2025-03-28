package com.pinup.pinup.domain.model

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

data class RatingGraph(
    val ratingArray: List<Int>
) {
    companion object {
        fun toModel(jsonObject: JsonObject): RatingGraph {
            val ratingArray = Array(5) { 0 }.toMutableList()
            jsonObject.keys.forEach {
                ratingArray[it.toInt()-1] = jsonObject[it]?.jsonPrimitive?.int ?: 0
            }
            return RatingGraph(
                ratingArray = ratingArray
            )
        }
    }
}