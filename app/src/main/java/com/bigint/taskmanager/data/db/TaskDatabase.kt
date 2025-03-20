package com.bigint.taskmanager.data.db

// Converter for Priority enum
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.bigint.taskmanager.domain.enums.Priority

@Database(entities = [TaskEntity::class], version = 1)
@TypeConverters(PriorityConverter::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): String = priority.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)
}