package com.bigint.taskmanager.domain.enums

enum class Priority(val value: Int) {
    LOW(1), MEDIUM(2), HIGH(3);

    companion object {
        fun fromValue(value: Int): Priority? {
            return Priority.entries.find { it.value == value }
        }
    }
}
