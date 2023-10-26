package com.example.newtrackmed.data.model

import java.time.LocalDateTime

data class LastTakenDose(
    val doseId: Int,
    val medicationId: Int,
    val createdTime: LocalDateTime,
    val dosage: Int
)
