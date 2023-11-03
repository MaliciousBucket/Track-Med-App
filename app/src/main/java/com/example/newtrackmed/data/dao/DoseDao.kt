package com.example.newtrackmed.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.model.DoseCount
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.DoseWithHistory
import com.example.newtrackmed.data.model.LastTakenDose
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

@Dao
interface DoseDao {
    // ----- Insert -----
    @Insert
    suspend fun insertDose(dose: DoseEntity): Long

//    ----- Update -----

    @Update
    suspend fun updateDose(dose: DoseEntity)

    @Query("UPDATE DoseEntity SET status = :newStatus, createdTime = :newCreatedTime WHERE doseId = :doseId")
    suspend fun updateDoseStatus(doseId: Int, newStatus: DoseStatus, newCreatedTime: LocalDateTime)


    //    ----- Delete -----
    @Delete
    suspend fun deleteDose(dose: DoseEntity)

    @Query("DELETE FROM DoseEntity WHERE doseId = :doseId")
    suspend fun deleteDoseById(doseId: Int)


//    ----- Retrieval Queries -----

    //    ----- By Date -----
    @Query("SELECT * FROM DoseEntity WHERE date(createdTime) = :selectedDate")
    fun getDosesForLocalDate(selectedDate: LocalDate): Flow<List<DoseEntity>>

    // ----- By Medication

    @Query("SELECT * FROM DoseEntity WHERE medicationId = :medicationId")
    fun getDosesByMedicationId(medicationId: Int): Flow<List<DoseEntity>>

    @Transaction
    @Query("SELECT * FROM DoseEntity WHERE doseId = :doseId")
    fun getDoseWithHistoryById(doseId: Int): Flow<DoseWithHistory>



    @Query(
        "SELECT medicationId, doseId, dosage, createdTime FROM DoseEntity" +
                " WHERE status = 0 AND medicationId IN (:medicationIds)" +
                " ORDER BY createdTime DESC LIMIT :limit"
    )
    fun getLastTakenDosesByMedIds(medicationIds: List<Int>, limit: Int): Flow<List<LastTakenDose>>

    @Transaction
    @Query("SELECT * FROM DoseEntity WHERE status = 0 AND medicationId = :medicationId")
    fun getLastTakenDoseByMedId(medicationId: Int): Flow<LastTakenDose>

    @Transaction
    @Query("SELECT * FROM DoseEntity WHERE date(createdTime) = :selectedDate")
    fun getDosesWithHistoryForSelectedDate(selectedDate: LocalDate): Flow<List<DoseEntity>>

    @Transaction
    @Query("SELECT * FROM DoseEntity WHERE doseId IN (SELECT doseId FROM DoseRescheduleHistory WHERE DATE(rescheduledTime) = :date)")
    fun getRescheduledDosesForDate(date: LocalDate): Flow<List<DoseEntity>>


    // ----- Utility -----


//----- SUSPEND ------

    @Query("SELECT DISTINCT medicationId FROM DoseEntity WHERE status = 0")
    suspend fun getAllTakenMedicationIds(): List<Int>

    @Query(
        "SELECT medicationId, doseId, dosage, createdTime FROM DoseEntity" +
                " WHERE status = 0 AND medicationId IN (:medicationIds)" +
                " ORDER BY createdTime DESC LIMIT :limit"
    )
    suspend fun getSuspendLastTakenDosesForMeds(medicationIds: List<Int>, limit: Int): List<LastTakenDose>

    @Query("SELECT * FROM DoseEntity WHERE date(createdTime) = :selectedDate")
    suspend fun getAllSuspendDosesForDate(selectedDate: LocalDate): List<DoseEntity>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity GROUP BY status")
    suspend fun getSuspendDoseCountByStatus(): List<DoseCount>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId = :medicationId GROUP BY status")
    suspend fun getSuspendDoseCountStatusByMedId(medicationId: Int): List<DoseCount>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId IN (:medicationIds) GROUP BY status")
    suspend fun getSuspendDoseCountsByMedIds(medicationIds: List<Int>): List<DoseCount>




    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId = :medicationId GROUP BY status LIMIT :limit")
    suspend fun getSuspendDoseCountsByMedIdWithLimit(medicationId: Int, limit: Int): List<DoseCount>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId IN (:medicationIds) GROUP BY status LIMIT :limit")
    suspend fun getSuspendDoseCountsByMultipleMedIdsWithLimit(medicationIds: List<Int>, limit: Int): List<DoseCount>












    //  -----     Reports ------
    @Query("SELECT status, COUNT(*) as count FROM DoseEntity GROUP BY status")
    fun getDoseCountByStatus(): Flow<List<DoseCount>>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId = :medicationId GROUP BY status")
    fun getDoseCountStatusByMedId(medicationId: Int): Flow<List<DoseCount>>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId IN (:medicationIds) GROUP BY status")
    fun getDoseCountsByMedIds(medicationIds: List<Int>): Flow<List<DoseCount>>


    @Transaction
    @Query("SELECT * FROM DoseEntity WHERE medicationId = :medicationId ORDER BY createdTime DESC LIMIT :limit")
    fun getDoseWithHistoryByMedId(medicationId: Int, limit: Int): Flow<List<DoseWithHistory>>

    @Transaction
    @Query("SELECT * FROM DoseEntity WHERE medicationId IN  (:medicationIds) ORDER BY createdTime DESC LIMIT :limit")
    fun getDoseWithHistoryByMedIds(medicationIds: List<Int>, limit: Int): Flow<List<DoseWithHistory>>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId = :medicationId GROUP BY status LIMIT :limit")
    fun getDoseCountsByMedIdWithLimit(medicationId: Int, limit: Int): Flow<List<DoseCount>>

    @Query("SELECT status, COUNT(*) as count FROM DoseEntity WHERE medicationId IN (:medicationIds) GROUP BY status LIMIT :limit")
    fun getDoseCountsByMultipleMedIdsWithLimit(medicationIds: List<Int>, limit: Int): Flow<List<DoseCount>>





}

