package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.PintsItem
import com.pinup.pinup.domain.model.PintsPageAble
import kotlinx.serialization.Serializable

@Serializable
data class PintsListResponse(
    val content: List<PintsItemResponse> = emptyList(),
    val last: Boolean = true,
    val totalPages: Int = -1,
    val totalElements: Int = -1,
) {
    companion object{
        fun PintsListResponse.toModel() : PintsPageAble{
            return PintsPageAble(
                content = content.map {
                    PintsItem(
                        pintsId = it.pintsId,
                        title = it.title,
                        content = it.content
                    )
                },
                last = last,
                totalPages = totalPages,
                totalElements = totalElements
            )
        }
    }
}

@Serializable
data class PintsItemResponse(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
)