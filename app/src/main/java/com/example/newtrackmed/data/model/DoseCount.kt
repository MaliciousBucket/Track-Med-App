package com.example.newtrackmed.data.model

import com.example.newtrackmed.data.entity.DoseStatus
import java.time.LocalDateTime

data class DoseCount(
    val status: DoseStatus,
    val count: Int
)
data class DoseCountWithId(
    val medicationId: Int,
    val status: DoseStatus,
    val count: Int,
)

data class DoseTimeRecord(
    val medicationId: Int,
    val doseId: Int,
    val dosage: Int,
    val createdTime: LocalDateTime,
)