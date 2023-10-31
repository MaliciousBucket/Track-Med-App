package com.example.newtrackmed.di

import android.content.Context
import androidx.room.RoomDatabase
import com.example.newtrackmed.data.dao.DoseDao
import com.example.newtrackmed.data.dao.DoseRescheduleHistoryDao
import com.example.newtrackmed.data.dao.FrequencyDao
import com.example.newtrackmed.data.dao.MedicationDao
import com.example.newtrackmed.data.database.TrackMedDatabase
import com.example.newtrackmed.data.database.getDatabase
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.FrequencyRepository
import com.example.newtrackmed.data.repository.MedicationDoseCompositeRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.ui.feature.addmedication.ContextResourceWrapper
import com.example.newtrackmed.ui.feature.addmedication.ResourceWrapper
import kotlinx.coroutines.CoroutineScope

interface AppModule {
    val database: RoomDatabase

    val doseDao: DoseDao
    val medicationDao: MedicationDao
    val frequencyDao: FrequencyDao
    val doseHistoryDao: DoseRescheduleHistoryDao


    val doseRepository: DoseRepository
    val medicationRepository: MedicationRepository
    val frequencyRepository: FrequencyRepository
    val compositeRepository: MedicationDoseCompositeRepository

    val resourceWrapper: ResourceWrapper

}

class AppModuleImpl(
    private val appContext: Context,
    private val coroutineScope: CoroutineScope
): AppModule {



    override val database: TrackMedDatabase = getDatabase(appContext)

    override val doseDao: DoseDao by lazy {
        database.doseDao()
    }

    override val medicationDao: MedicationDao by lazy{
        database.medicationDao()
    }

    override val frequencyDao: FrequencyDao by lazy{
        database.frequencyDao()
    }

    override val doseHistoryDao: DoseRescheduleHistoryDao by lazy {
        database.doseHistoryDao()
    }


    override val doseRepository: DoseRepository by lazy {
        DoseRepository(doseDao, doseHistoryDao)
    }

    override val medicationRepository: MedicationRepository by lazy{
        MedicationRepository(medicationDao)
    }
    override val frequencyRepository: FrequencyRepository by lazy {
        FrequencyRepository(frequencyDao)
    }

    override val compositeRepository: MedicationDoseCompositeRepository by lazy {
        MedicationDoseCompositeRepository(
            doseRepository,
            medicationRepository,
            frequencyRepository,
            medicationDao,
            doseDao,
            frequencyDao,
            coroutineScope
        )
    }

    override val resourceWrapper: ResourceWrapper by lazy {
        ContextResourceWrapper(appContext)
    }




}