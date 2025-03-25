package com.pinup.pinup.remote.api

import com.pinup.pinup.data.response.GetDetailPlaceResponse
import com.pinup.pinup.data.response.GetReviewedPlacesResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.SearchPlacesResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

class PlacesApi(
    private val httpClient: HttpClient
) {
    suspend fun getPlaces(
        sort: SortType,
        category: Category,
        swLatitude: String,
        swLongitude: String,
        neLatitude: String,
        neLongitude: String,
        currentLatitude: String?,
        currentLongitude: String?,
    ): PResult<PResponse<List<GetReviewedPlacesResponse>>> {
        val response = httpClient.get("api/places") {
            url {
                parameters {
                    append("sort", sort.toString())
                    append("category", category.toString())
                    append("swLatitude", swLatitude)
                    append("swLongitude", swLongitude)
                    append("neLatitude", neLatitude)
                    append("neLongitude", neLongitude)
                    currentLatitude?.let {
                        append("currentLatitude", currentLatitude)
                    }
                    currentLongitude?.let {
                        append("currentLongitude", currentLongitude)
                    }
                }
            }
        }
        return response.body()
    }

    suspend fun searchPlaces(query: String): PResult<PResponse<List<SearchPlacesResponse>>> {
        val response = httpClient.get("api/places/keyword") {
            url {
                parameters.append("query", query)
            }
        }
        return response.body()
    }

    suspend fun getDetailPlace(
        kakaoPlaceId: String,
        currentLatitude: String?,
        currentLongitude: String?,
    ): PResult<PResponse<GetDetailPlaceResponse>> {
        val response = httpClient.get("api/places/$kakaoPlaceId") {
            url {
                parameters {
                    currentLatitude?.let {
                        append("currentLatitude", currentLatitude)
                    }
                    currentLongitude?.let {
                        append("currentLongitude", currentLongitude)
                    }
                }
            }
        }
        return response.body()
    }
}