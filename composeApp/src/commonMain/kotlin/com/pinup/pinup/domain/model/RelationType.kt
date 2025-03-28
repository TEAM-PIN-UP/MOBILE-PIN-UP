package com.pinup.pinup.domain.model

enum class RelationType {
    SELF, FRIEND, PENDING, STRANGER, NONE;
    companion object {
        fun of(name: String): RelationType {
            return if (RelationType.entries.any { it.name == name }) {
                RelationType.valueOf(name)
            } else {
                NONE
            }
        }
    }
}