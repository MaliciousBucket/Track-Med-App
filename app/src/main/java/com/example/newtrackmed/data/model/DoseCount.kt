package com.example.newtrackmed.data.model

import com.example.newtrackmed.data.entity.DoseStatus

data class DoseCount(
    val status: DoseStatus,
    val count: Int
)
