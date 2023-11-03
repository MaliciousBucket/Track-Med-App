package com.example.newtrackmed.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newtrackmed.data.entity.DoseRescheduleHistory

@Dao
interface DoseRescheduleHistoryDao {

    @Insert
    suspend fun insertDoseRescheduleHistory(doseRescheduleHistory: DoseRescheduleHistory)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDoseRescheduleHistory(doseRescheduleHistory: DoseRescheduleHistory)

    @Delete
    suspend fun deleteDoseRescheduleHistory(doseRescheduleHistory: DoseRescheduleHistory)

    @Query("UPDATE DoseRescheduleHistory SET rescheduleReason = :newReason WHERE historyId = :historyId")
    suspend fun updateRescheduleReason(historyId: Int, newReason: String?)
}