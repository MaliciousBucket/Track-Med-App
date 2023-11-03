package com.example.newtrackmed.data.repository

import android.util.Log
import co.yml.charts.common.extensions.isNotNull
import com.example.newtrackmed.data.dao.DoseDao
import com.example.newtrackmed.data.dao.DoseRescheduleHistoryDao
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseRescheduleHistory
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.model.DoseCount
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.DoseWithHistory
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.RecentDoseDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        createdTime: LocalDateTime
    ) {

    }

    suspend fun insertDose(doseEntity: DoseEntity): Long {
        return doseDao.insertDose(doseEntity)
    }

    suspend fun updateDose(doseEntity: DoseEntity) {
        doseDao.updateDose(doseEntity)
    }

//    suspend fun updateDoseStatus(
//        doseId: Int,
//        doseStatus: DoseStatus){
//        val status = doseStatus.ordinal
//        doseDao.updateDoseStatus(doseId, status)
//    }

//    suspend fun rescheduleDose(
//        doseId: Int,
//        originalTime: LocalDateTime,
//        rescheduledTo: LocalDateTime,
//        reason: String?){
//        val status = DoseStatus.RESCHEDULED.ordinal
//
//        val history = DoseRescheduleHistory(
//            historyId = 0,
//            doseId = doseId,
//            originalTime = originalTime,
//            rescheduledTime = rescheduledTo,
//            rescheduleReason = reason
//        )
//        doseDao.updateDoseStatus(doseId, status)
//
//        doseRescheduleHistoryDao.insertDoseRescheduleHistory(history)
//
//    }
//    ----- DELETE -----

    suspend fun deleteDose(dose: DoseEntity) {
        doseDao.deleteDose(dose)
    }

    suspend fun deleteDoseById(doseId: Int) {
        doseDao.deleteDoseById(doseId)
    }

//    ----- Query -----

    fun getDoseWithHistoryById(doseId: Int): Flow<DoseWithHistory> =
        doseDao.getDoseWithHistoryById(doseId)


//    ----- By Date -----

    fun getDosesForSelectedDate(selectedDateTime: LocalDateTime): Flow<List<DoseEntity>> =
        flow {
            coroutineScope {
                val localDate = selectedDateTime.toLocalDate()

                val doses = mutableListOf<DoseEntity>()
                val rescheduledDoses = mutableListOf<DoseEntity>()

                val dosesJob = launch {
                    doseDao.getDosesWithHistoryForSelectedDate(localDate).collect {
                        doses.addAll(it)
                    }
                }

                val rescheduledDosesJob = launch {
                    doseDao.getRescheduledDosesForDate(localDate).collect {
                        rescheduledDoses.addAll(it)
                    }
                }

                dosesJob.join()
                rescheduledDosesJob.join()

                emit(doses + rescheduledDoses)
            }
        }


    fun getRescheduledDosesForDate() {

    }

//    ----- Last Taken -----

    fun getLastTakenDoseForMed(medicationId: Int): Flow<LastTakenDose> =
        doseDao.getLastTakenDoseByMedId(medicationId)


    suspend fun getLastTakenDosesForAllMeds(): List<LastTakenDose>
    {
        val medicationIds = doseDao.getAllTakenMedicationIds()
        return withContext(Dispatchers.IO){
            doseDao.getSuspendLastTakenDosesForMeds(medicationIds, 1)
        }
    }



    //Suspend Reports --------------------------------------------------------

    suspend fun getLastTakenDosesForMedIds(medicationIds: List<Int>, limit: Int): List<LastTakenDose>
    {
        return withContext(Dispatchers.IO){
            doseDao.getSuspendLastTakenDosesForMeds(medicationIds, limit)
        }
    }




    suspend fun getSuspendDoseCounts(): List<DoseCount> {
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountByStatus()
        }.onEach { Log.d("Getting Dose Counts", "Collecting Dose Count") }
    }




    suspend fun getSuspendDoseCountsByMedId(medicationId: Int): List<DoseCount> {

        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountStatusByMedId(medicationId)
        }
    }


    suspend fun getSuspendDoseCountsByMultipleMedIds(medicationIds: List<Int>): List<DoseCount>
    {
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountsByMedIds(medicationIds)
        }
    }







    suspend fun getSuspendDoseCountsByMedIdWithLimit(medicationId: Int, limit: Int):List<DoseCount>
    {
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountsByMedIdWithLimit(medicationId, limit)
        }
    }

    suspend fun getSuspendDoseCountsByMultipleMedIdsWithLimit(medicationIds: List<Int>, limit: Int):List<DoseCount>
    {
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountsByMultipleMedIdsWithLimit(medicationIds, limit)
        }
    }


//    ----------------------------------------------------------------------------------------






    //Reports
    fun getDoseCounts(): Flow<List<DoseCount>> =
        doseDao.getDoseCountByStatus().flowOn(Dispatchers.IO)

    fun getDoseCountsByMedId(medicationId: Int): Flow<List<DoseCount>> =
        doseDao.getDoseCountStatusByMedId(medicationId).flowOn(Dispatchers.IO)

    fun getDoseCountsByMultipleMedIds(medicationIds: List<Int>) : Flow<List<DoseCount>> =
        doseDao.getDoseCountsByMedIds(medicationIds).flowOn(Dispatchers.IO)

    fun getLastTakenDoseByMedicationIds(medicationIds: List<Int>, limit: Int) : Flow<List<LastTakenDose>> =
        doseDao.getLastTakenDosesByMedIds(medicationIds, limit).flowOn(Dispatchers.IO)

    fun getDosesWithHistoryForMedId(medicationId: Int, limit: Int) : Flow<List<DoseWithHistory>> =
        doseDao.getDoseWithHistoryByMedId(medicationId, limit).flowOn(Dispatchers.IO)

    fun getDosesWithHistoryForMedIds(medicationIds: List<Int>, limit: Int) : Flow<List<DoseWithHistory>> =
        doseDao.getDoseWithHistoryByMedIds(medicationIds, limit).flowOn(Dispatchers.IO)

    fun getDoseCountsByMedIdWithLimit(medicationId: Int, limit: Int): Flow<List<DoseCount>>
    = doseDao.getDoseCountsByMedIdWithLimit(medicationId, limit).flowOn(Dispatchers.IO)

    fun getDoseCountsByMultipleMedIdsWithLimit(medicationIds: List<Int>, limit: Int): Flow<List<DoseCount>> =
        doseDao.getDoseCountsByMultipleMedIdsWithLimit(medicationIds, limit).flowOn(Dispatchers.IO)

}