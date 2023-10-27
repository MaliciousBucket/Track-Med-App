package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.dao.DoseDao
import com.example.newtrackmed.data.dao.DoseRescheduleHistoryDao
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseRescheduleHistory
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.DoseWithHistory
import com.example.newtrackmed.data.model.LastTakenDose
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class DoseRepository(
    private val doseDao: DoseDao,
    private val doseRescheduleHistoryDao: DoseRescheduleHistoryDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun insertTakenDose(
        medicationId: Int,
        status: DoseStatus,
        dosage: Int,
        createdTime: LocalDateTime){

    }

    suspend fun insertDose(doseEntity: DoseEntity): Long{
        return doseDao.insertDose(doseEntity)
    }

    suspend fun updateDose(doseEntity: DoseEntity){
        doseDao.updateDose(doseEntity)
    }

    suspend fun updateDoseStatus(
        doseId: Int,
        doseStatus: DoseStatus){
        val status = doseStatus.ordinal
        doseDao.updateDoseStatus(doseId, status)
    }

    suspend fun rescheduleDose(
        doseId: Int,
        originalTime: LocalDateTime,
        rescheduledTo: LocalDateTime,
        reason: String?){
        val status = DoseStatus.RESCHEDULED.ordinal

        val history = DoseRescheduleHistory(
            historyId = 0,
            doseId = doseId,
            originalTime = originalTime,
            rescheduledTime = rescheduledTo,
            rescheduleReason = reason
        )
        doseDao.updateDoseStatus(doseId, status)

        doseRescheduleHistoryDao.insertDoseRescheduleHistory(history)

    }
//    ----- DELETE -----

    suspend fun deleteDose(dose: DoseEntity){
        doseDao.deleteDose(dose)
    }

    suspend fun deleteDoseById(doseId: Int){
        doseDao.deleteDoseById(doseId)
    }

//    ----- Query -----

    fun getDoseWithHistoryById(doseId: Int) : Flow<DoseWithHistory> =
        doseDao.getDoseWithHistoryById(doseId)



//    ----- By Date -----

    fun getDosesForSelectedDate(selectedDateTime: LocalDateTime): Flow<List<DoseEntity>> =
        flow {
            coroutineScope {
                val localDate = selectedDateTime.toLocalDate()

                val doses = mutableListOf<DoseEntity>()
                val rescheduledDoses = mutableListOf<DoseEntity>()

                // Asynchronously collect the doses
                val dosesJob = launch {
                    doseDao.getDosesWithHistoryForSelectedDate(localDate).collect {
                        doses.addAll(it)
                    }
                }

                // Asynchronously collect the rescheduled doses
                val rescheduledDosesJob = launch {
                    doseDao.getRescheduledDosesForDate(localDate).collect {
                        rescheduledDoses.addAll(it)
                    }
                }

                // Wait for both jobs to complete
                dosesJob.join()
                rescheduledDosesJob.join()

                // Combine and emit the lists
                emit(doses + rescheduledDoses)
            }
        }






    fun getRescheduledDosesForDate(){

    }

//    ----- Last Taken -----

    fun getLastTakenDoseForMed(medicationId: Int): Flow<LastTakenDose> =
        doseDao.getLastTakenDoseByMedId(medicationId)



    fun getLastTakenDosesForMedIds(medicationIds: List<Int>)  {

    }

    fun getLastTakenDosesForAllMeds(){

    }




}