package com.example.newtrackmed.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.newtrackmed.data.entity.FrequencyEntity

@Dao
interface FrequencyDao {

    @Insert
    suspend fun insertFrequency(frequencyEntity: FrequencyEntity)
}