package com.example.newtrackmed.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.model.Medication
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Insert
    suspend fun insertMedication(medication: MedicationEntity): Long

    @Update
    suspend fun updateMedication(medication: MedicationEntity)

    @Query("SELECT * FROM MedicationEntity")
    fun getAllMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM MedicationEntity WHERE isActive = 1")
    fun  getAllActiveMedications(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM MedicationEntity WHERE id = :medicationId")
    fun getMedicationById(medicationId: Int): Flow<MedicationEntity>




}