package com.example.newtrackmed.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.MedicationEntity

data class MedicationWithFrequency(
    @Embedded val medicationEntity: MedicationEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "medicationId"
    )
    val frequency: FrequencyEntity
)
