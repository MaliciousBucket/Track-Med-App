package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.dao.MedicationDao
import com.example.newtrackmed.data.entity.MedicationEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MedicationRepository (
    private val medicationDao: MedicationDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
){
    fun getMedicationById(medicationId: Int) : Flow<MedicationEntity> =
        medicationDao.getMedicationById(medicationId)

    fun getAllMedications(): Flow<List<MedicationEntity>> =
        medicationDao.getAllMedications()

    fun getAllActiveMedications(): Flow<List<MedicationEntity>> =
        medicationDao.getAllActiveMedications()

}