package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.dao.FrequencyDao
import com.example.newtrackmed.data.entity.FrequencyEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class FrequencyRepository(
    private val frequencyDao: FrequencyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun insertFrequency(frequency: FrequencyEntity){
        return frequencyDao.insertFrequency(frequency)
    }

    fun updateFrequency(){

    }

    fun getAllFrequencies(): Flow<List<FrequencyEntity>> =
    frequencyDao.getAllFrequencies()


    fun getFrequencyByMedicationId(){

    }

}