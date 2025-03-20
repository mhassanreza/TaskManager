package com.bigint.taskmanager.domain.enums

enum class SortOption(val value: Int, val title: String) {
    PRIORITY(1, "Priority"), DUE_DATE(2, "Due Date"), TITLE(3, "Title");

    companion object {
        fun fromValue(value: Int): SortOption {
            return SortOption.entries.find { it.value == value } ?: PRIORITY
        }
    }
}
