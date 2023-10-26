package com.example.newtrackmed.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [
        ForeignKey(
            entity= DoseEntity::class,
            parentColumns = ["doseId"],
            childColumns = ["doseId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["doseId"])]
)
data class DoseRescheduleHistory (
    @PrimaryKey(autoGenerate = true)
    val historyId: Int,
    val doseId: Int,
    val originalTime: LocalDateTime,
    val rescheduledTime: LocalDateTime,
    val rescheduleReason: String?
)