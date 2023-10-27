package com.example.newtrackmed.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.DoseWithHistory
import com.example.newtrackmed.data.model.LastTakenDose
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DoseDao {
    // ----- Insert -----
    @Insert
    suspend fun insertDose(dose: DoseEntity) : Long

//    ----- Update -----

    @Update
    suspend fun updateDose(dose: DoseEntity)

    @Query("UPDATE DoseEntity SET status = :newStatus WHERE doseId = :doseId")
    suspend fun updateDoseStatus(doseId: Int, newStatus: Int)

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


    @Query("SELECT medicationId, doseId, dosage, createdTime FROM DoseEntity" +
            " WHERE status = 0 AND medicationId IN (:medicationIds)" +
            " ORDER BY createdTime DESC LIMIT :limit")
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

}

    // ----- Utility -----

