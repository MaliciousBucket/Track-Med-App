package com.example.newtrackmed.data.repository

import com.example.newtrackmed.data.dao.FrequencyDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FrequencyRepository(
    private val frequencyDao: FrequencyDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    fun insertFrequency(){

    }

    fun updateFrequency(){

    }

    fun getAllFrequencies(){

    }

    fun getFrequencyByMedicationId(){

    }



}