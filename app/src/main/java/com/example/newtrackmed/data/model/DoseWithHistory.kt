package com.example.newtrackmed.data.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseRescheduleHistory

data class DoseWithHistory (
    @Embedded val doseEntity: DoseEntity,
    @Relation(
        parentColumn = "doseId",
        entityColumn = "doseId"
    )
    val histories: List<DoseRescheduleHistory>
)

fun DoseEntity.mapToDoseWithHistory(): DoseWithHistory {
    return DoseWithHistory(
        doseEntity = this,
        histories = emptyList()
    )
}
