package com.bigint.taskmanager.domain.enums

enum class FilterOption(val value: Int, val title: String) {
    ALL(1, "All"), COMPLETED(2, "Completed"), PENDING(3, "Pending");

    companion object {
        fun fromValue(value: Int): FilterOption {
            return FilterOption.entries.find { it.value == value } ?: ALL
        }
    }
}