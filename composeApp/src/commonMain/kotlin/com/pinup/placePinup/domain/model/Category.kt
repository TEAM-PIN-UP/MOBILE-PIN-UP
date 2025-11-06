package com.pinup.placePinup.domain.model

enum class Category {
    ALL,
    RESTAURANT,
    CAFE,
    NONE;
    companion object {
        fun of(name: String): Category {
            return if (entries.any { it.name == name }) {
                Category.valueOf(name)
            } else {
                NONE
            }
        }
    }
}