package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.dao.MedicationDao
import com.example.newtrackmed.data.entity.MedicationEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class MedicationRepository (
    private val medicationDao: MedicationDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
){
    suspend fun insertMedicationEntity(medication: MedicationEntity): Long{
       return medicationDao.insertMedication(medication)
    }
    fun getMedicationById(medicationId: Int) : Flow<MedicationEntity> =
        medicationDao.getMedicationById(medicationId)

    fun getAllMedications(): Flow<List<MedicationEntity>> =
        medicationDao.getAllMedications().flowOn(Dispatchers.IO)

    fun getAllActiveMedications(): Flow<List<MedicationEntity>> =
        medicationDao.getAllActiveMedications().flowOn(Dispatchers.IO)



    suspend fun getAllSuspendMedications(): List<MedicationEntity> {
        return withContext(Dispatchers.IO){
            medicationDao.getAllSuspendMedications()
        }
    }

}