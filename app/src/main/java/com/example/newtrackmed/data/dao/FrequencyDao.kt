package com.example.newtrackmed.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.newtrackmed.data.entity.FrequencyEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface FrequencyDao {

    @Insert
    suspend fun insertFrequency(frequencyEntity: FrequencyEntity)

    @Query("SELECT * FROM FrequencyEntity")
    fun getAllFrequencies(): Flow<List<FrequencyEntity>>

    @Query("SELECT * FROM FrequencyEntity")
    suspend fun getAllSuspendFrequencies(): List<FrequencyEntity>

    @Query("SELECT * FROM FrequencyEntity WHERE medicationId = :medicationId")
    fun getFrequencyByMedicationId(medicationId: Int): Flow<FrequencyEntity>
}