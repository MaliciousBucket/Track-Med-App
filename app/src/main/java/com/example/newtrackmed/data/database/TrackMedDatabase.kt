package com.example.newtrackmed.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newtrackmed.data.dao.DoseDao
import com.example.newtrackmed.data.dao.DoseRescheduleHistoryDao
import com.example.newtrackmed.data.dao.FrequencyDao
import com.example.newtrackmed.data.dao.MedicationDao
import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseRescheduleHistory
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.util.DoseStatusConverter
import com.example.newtrackmed.util.FrequencyTypeConverter
import com.example.newtrackmed.util.IntListConverter
import com.example.newtrackmed.util.LocalDateConverter
import com.example.newtrackmed.util.LocalDateTimeConverter
import com.example.newtrackmed.util.LocalTimeConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Database(
    version = 2,
    entities = [
        MedicationEntity::class,
        FrequencyEntity::class,
        DoseEntity::class,
        DoseRescheduleHistory::class
    ]
)

@TypeConverters(
    LocalDateTimeConverter::class,
    LocalDateConverter::class,
    LocalTimeConverter::class,
    DoseStatusConverter::class,
    FrequencyTypeConverter::class,
    IntListConverter::class
)



abstract class TrackMedDatabase: RoomDatabase() {
    abstract fun medicationDao() : MedicationDao
    abstract fun frequencyDao() : FrequencyDao
    abstract fun doseDao() : DoseDao

    abstract fun doseHistoryDao(): DoseRescheduleHistoryDao
}
private lateinit var INSTANCE: TrackMedDatabase

fun getDatabase(context: Context): TrackMedDatabase {
    synchronized(TrackMedDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                TrackMedDatabase::class.java,
                "trackmeddb"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val medicationDao = INSTANCE.medicationDao()
                            val doseDao = INSTANCE.doseDao()
                            val frequencyDao = INSTANCE.frequencyDao()
                            val doseHistoryDao = INSTANCE.doseHistoryDao()


                            val medicationNames = listOf("Ibuprofen", "Paracetamol", "Aspirin", "Acetaminophen", "Codeine")
                            val medicationTypes = listOf("Pill", "Sachet", "Capsule", "Liquid")
                            val dosageUnits = listOf("mg", "ml")
                            val timeToTake = listOf(LocalTime.of(8, 0), LocalTime.of(12, 0), LocalTime.of(18, 0), LocalTime.of(22, 0))
                            val instructions = listOf("Take with food", "Take on an empty stomach", "Take with water")
                            val notes = listOf("Check for allergies", "Not for kids below 12", "Keep away from sunlight")
                            val startDate = LocalDate.now()
                            val endDate = startDate.plusDays(90)
                            val frequencyIntervals = listOf(1, 2, 3, 4, 5, 6, 7, 10, 15, 30)
                            val frequencyTypes = listOf(FrequencyType.DAILY, FrequencyType.EVERY_OTHER, FrequencyType.EVERY_X_DAYS, FrequencyType.WEEK_DAYS, FrequencyType.MONTH_DAYS)

                            val numMedications = 10

                            for (i in 0 until numMedications) {
                                val medication = MedicationEntity(
                                    id = 0,  // Auto-generated
                                    name = medicationNames.random(),
                                    type = medicationTypes.random(),
                                    dosage = (100..500).random(),
                                    dosageUnit = dosageUnits.random(),
                                    unitsTaken = (1..3).random(),
                                    timeToTake = timeToTake.random(),
                                    instructions = instructions.random(),
                                    notes = notes.random(),
                                    startDate = startDate,
                                    endDate = endDate,
                                    isActive = true,
                                    isDeleted = false
                                )


                                val medicationId = medicationDao.insertMedication(medication)


                                val numDoses = (3..10).random()
                                for (j in 0 until numDoses) {
                                    val dose = DoseEntity(
                                        doseId = 0,
                                        medicationId = medicationId.toInt(),
                                        status = DoseStatus.values().random(),
                                        dosage = medication.dosage,
                                        createdTime = LocalDateTime.now().plusDays(j.toLong())
                                    )
                                    doseDao.insertDose(dose)
                                }


                                val frequency = FrequencyEntity(
                                    id = 0,
                                    medicationId = medicationId.toInt(),
                                    frequencyIntervals = List((1..3).random()) { frequencyIntervals.random() },
                                    frequencyType = frequencyTypes.random(),
                                    asNeeded = false
                                )
                                frequencyDao.insertFrequency(frequency)
                            }
                        }
                    }
                }).build()
        }
    }
    return INSTANCE
}



