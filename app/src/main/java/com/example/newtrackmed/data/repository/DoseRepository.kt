package com.example.newtrackmed.data.repository

import android.util.Log
import co.yml.charts.common.extensions.isNotNull
import com.example.newtrackmed.data.dao.DoseDao
import com.example.newtrackmed.data.dao.DoseRescheduleHistoryDao
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseRescheduleHistory
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.model.DoseCount
import com.example.newtrackmed.data.model.DoseCountWithId
import com.example.newtrackmed.data.model.DoseTimeRecord
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


    suspend fun deleteDose(dose: DoseEntity) {
        doseDao.deleteDose(dose)
    }

    suspend fun deleteDoseById(doseId: Int) {
        doseDao.deleteDoseById(doseId)
    }

//    ----- Query -----

    fun getDoseWithHistoryById(doseId: Int): Flow<DoseWithHistory> =
        doseDao.getDoseWithHistoryById(doseId)


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


//    --------------------------Reports with status by Med Id--------------------------------------------------------------

    suspend fun getSuspendDoseCountWithIdByStatus(): List<DoseCountWithId>{
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountWithIdByStatus()
        }
    }

    suspend fun getSuspendDoseCountWithIdStatusByMedId(medicationId: Int): List<DoseCountWithId>{
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountWithIdStatusByMedId(medicationId)
        }
    }

    suspend fun getSuspendDoseCountWithIdByMedIds(medicationIds: List<Int>): List<DoseCountWithId>{
        return withContext(Dispatchers.IO){
            doseDao.getSuspendDoseCountWithIdByMedIds(medicationIds)
        }
    }

    suspend fun getDoseTimeRecordsForLastWeek(medicationIds: List<Int>, startDate: LocalDateTime): List<DoseTimeRecord> {
        return withContext(Dispatchers.IO){
            doseDao.getDoseTimeRecordsForLastWeek(medicationIds, startDate)
        }
    }




}