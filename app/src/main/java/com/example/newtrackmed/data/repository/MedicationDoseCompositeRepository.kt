package com.example.newtrackmed.data.repository

import android.util.Log
import com.example.newtrackmed.data.dao.DoseDao
import com.example.newtrackmed.data.dao.FrequencyDao
import com.example.newtrackmed.data.dao.MedicationDao
import com.example.newtrackmed.data.entity.DoseEntity
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.entity.FrequencyEntity
import com.example.newtrackmed.data.entity.FrequencyType
import com.example.newtrackmed.data.entity.MedicationEntity
import com.example.newtrackmed.data.entity.mapToDoseEntity
import com.example.newtrackmed.data.model.DoseViewData
import com.example.newtrackmed.data.model.DoseWithHistory
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.Medication
import com.example.newtrackmed.data.model.MyMedicationsViewData
import com.example.newtrackmed.data.model.UpdateDoseData
import com.example.newtrackmed.data.model.asAsNeededDisplayDoseViewData
import com.example.newtrackmed.data.model.asDisplayDoseViewData
import com.example.newtrackmed.data.model.asDisplayModel
import com.example.newtrackmed.data.model.mapDoseStatusToChipStatus
import com.example.newtrackmed.data.model.mapToMyMedicationsViewData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class DoseFeed(
    val asNeededViewData: List<DoseViewData>,
    val dosesForDateViewData: List<DoseViewData>,
    val scheduledMedsViewData: List<DoseViewData>,
) {
    val allData: List<DoseViewData> =
        asNeededViewData + dosesForDateViewData + scheduledMedsViewData
}

class MedicationDoseCompositeRepository(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository,
    private val frequencyRepository: FrequencyRepository,

    private val medicationDao: MedicationDao,
    private val doseDao: DoseDao,
    private val frequencyDao: FrequencyDao,

    private val coroutineScope: CoroutineScope
) {

    private val _allFrequencies = MutableStateFlow<List<FrequencyEntity>>(emptyList())

    private val _allMedications = MutableStateFlow<List<MedicationEntity>>(emptyList())

    private  val _allActiveMedications = MutableStateFlow<List<MedicationEntity>>(emptyList())

    private val _dosesForData = MutableStateFlow<List<DoseEntity?>>(emptyList())




    suspend fun getAllFrequencies() = withContext(Dispatchers.IO){
        val frequencies = frequencyDao.getAllSuspendFrequencies()
        _allFrequencies.update { frequencies }
    }

    suspend fun getAllMedications() = withContext(Dispatchers.IO) {
        val medications = medicationDao.getAllSuspendMedications()
        _allMedications.update { medications }
    }

    suspend fun getAllActiveMedications() = withContext(Dispatchers.IO){
        val medications = medicationDao.getAllSuspendActiveMedications()
        Log.d("Debug Active Meds", "Active Meds: $medications")
        _allActiveMedications.update { medications }
    }

    suspend fun getAllDosesForDate(selectedDate: LocalDateTime) = withContext(Dispatchers.IO){
        val localDate = selectedDate.toLocalDate()
        val doses = doseDao.getAllSuspendDosesForDate(localDate)
        _dosesForData.update { doses }
    }

    suspend fun getTestData() = withContext(Dispatchers.IO) {
        val medicationList = _allMedications.value
        val newData = medicationList.map { it.asDisplayDoseViewData() }
        _viewData.value = newData
    }

     fun testMeds() : Flow<List<DoseViewData>> {
        return medicationDao.getAllActiveMedications().map { medications ->
            medications.map { it.asDisplayDoseViewData() }.onEach {
                Log.d("Debug Composite", "Returning: $medications")
            }
        }
     }

    private val _testmedsFlow: Flow<List<MedicationEntity>> =
        medicationDao.getAllMedications()

    private val _testFrequencyFlow: Flow<List<FrequencyEntity>> =
        frequencyDao.getAllFrequencies()
//    private val _testDoseFlow: Flow<List<DoseEntity>> =

    private val _medIdsForDate =  MutableStateFlow(setOf<Int>())

    private fun filterLogic(selectedDate: LocalDateTime): Flow<List<Int>>
    {
        return combine(
            _testmedsFlow,
            _testFrequencyFlow
        ) { medications, frequencies ->
            val medsAndFreqs = medications.map { medication ->
                medication to frequencies.find { it.medicationId == medication.id }
            }
            medsAndFreqs.filter { (medication, frequency) ->
                if (frequency == null) {
                    return@filter false
                }
                if (frequency.asNeeded) {
                    return@filter true
                }
//                val interval = frequency.frequencyIntervals
                val daysBetween = ChronoUnit.DAYS.between(medication.startDate, selectedDate)
                val selectedDayOfWeek = selectedDate.dayOfWeek.value
                val selectedDayOfMonth = selectedDate.dayOfMonth

                when (frequency.frequencyType) {
                    FrequencyType.DAILY -> {
                        return@filter true
                    }

                    FrequencyType.EVERY_OTHER -> {
                        return@filter daysBetween % 2 == 0L
                    }

                    FrequencyType.EVERY_X_DAYS -> {
                        return@filter daysBetween % frequency.frequencyIntervals[0] == 0L
                    }

                    FrequencyType.WEEK_DAYS -> {
                        return@filter frequency.frequencyIntervals.contains(selectedDayOfWeek)
                    }

                    FrequencyType.MONTH_DAYS -> {
                        return@filter frequency.frequencyIntervals.contains(selectedDayOfMonth)
                    }
                }
                false
            }.map { (medication, _) -> medication.id }
        }.flowOn(Dispatchers.IO)
    }

    fun createTestData( selectedDate: LocalDateTime): Flow<List<DoseViewData>> {
        Log.d("All Doses for Today", "${_dosesForData.value}")
        val selectedLocalDate = selectedDate.toLocalDate()
        val dosesForDate = doseDao.getDosesForLocalDate(selectedLocalDate)

        return flow {
            coroutineScope.launch {
                filterLogic(selectedDate).flowOn(Dispatchers.IO).collect { filteredIds ->
                    val intIds = filteredIds.mapNotNull { it.toInt() }.toSet()
                    Log.d("Debug Filtering", "Ids that made it: $intIds")
                    _medIdsForDate.update { intIds }
                }
            }

            emitAll(
                combine(
                    _medIdsForDate,
                    _allMedications,
                    dosesForDate,
                    _allFrequencies
                ) { filteredIds, allMeds, dosesForDate, allFrequencies ->
                    val filteredMeds = allMeds.filter { filteredIds.contains(it.id) }
                    filteredMeds.flatMap { medication ->
                        val correspondingDoses = dosesForDate.filter { it.medicationId == medication.id }
                        val correspondingFrequency = allFrequencies.find { it.medicationId == medication.id }
                        val innerList = mutableListOf<DoseViewData>()

                        if (correspondingFrequency?.asNeeded == true || correspondingDoses.isEmpty()) {
                            innerList.add(medication.asDisplayDoseViewData())
                        }
                        innerList.addAll(correspondingDoses.map { dose ->
                            DoseViewData(
                                medicationId = medication.id,
                                doseId = dose.doseId,
                                name = medication.name,
                                type = medication.type,
                                dosage = medication.dosage,
                                dosageUnit = medication.dosageUnit,
                                unitsTaken = medication.unitsTaken,
                                doseTime = dose.createdTime.toLocalTime(),
                                chipStatus = mapDoseStatusToChipStatus(dose.status)  // Assuming you have this function
                            )
                        })

                        innerList
                    }
                }
            )
        }
            .flowOn(Dispatchers.IO)
    }

    suspend fun setup() {
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                getAllFrequencies()
                getAllMedications()
                getAllActiveMedications()
                getAllDosesForDate(LocalDateTime.now())
                getTestData()
            }
        }
    }


    private val _viewData = MutableStateFlow<List<DoseViewData?>>(emptyList())

    fun observeDoseFeed(): Flow<List<DoseViewData?>> = _viewData

    fun getUpdateDoseData(medicationId: Int, doseId: Int?): Flow<UpdateDoseData> {
        Log.d("Debug Dialog Data", "Entered with med: $medicationId, dose: $doseId")
        if(doseId == null) {
            Log.d("Debug Dialog Data", "Entered null")
            val lastDoseFlow: Flow<LastTakenDose> =
                doseRepository.getLastTakenDoseForMed(medicationId)
            val medicationFlow: Flow<MedicationEntity> =
                medicationRepository.getMedicationById(medicationId)



            return combine(medicationFlow, lastDoseFlow) { medication, lastDose ->
                Log.d("Debug Dialog Data", "Returning: $medication LastDose: $lastDose")
                UpdateDoseData(
                    medication = medication,
                    dose = null,
                    lastTakenDose = lastDose
                )
            }
        }
        Log.d("Debug Dialog Data", "Dose not null")
        val lastDoseFlow: Flow<LastTakenDose> = doseRepository.getLastTakenDoseForMed(medicationId)
        val medicationFlow: Flow<MedicationEntity> = medicationRepository.getMedicationById(medicationId)
        val doseWithHistoryFlow: Flow<DoseWithHistory> = doseRepository.getDoseWithHistoryById(doseId)

        return combine(medicationFlow, lastDoseFlow, doseWithHistoryFlow) { medication, lastDose, doseWithHistory ->
            Log.d("Debug Dialog Data", "Returning: $medication, Dose: $doseWithHistory LastDose: $lastDose")
            UpdateDoseData(
                medication = medication,
                dose = doseWithHistory,
                lastTakenDose = lastDose
            )
        }.distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .onEach { Log.d("Debug Dialog Data", "Emitting Data") }

    }

    suspend fun missDose(doseId: Int, missedAt: LocalDateTime){
        withContext(Dispatchers.IO){
            doseDao.updateDoseStatus(doseId, DoseStatus.MISSED, missedAt)
        }
    }

    suspend fun deleteDose(doseId: Int){
        withContext(Dispatchers.IO){
            doseDao.deleteDoseById(doseId)
        }
    }

    suspend fun updateDoseStatus(doseId: Int, status: DoseStatus, updateTime: LocalDateTime){
        doseDao.updateDoseStatus(doseId, status, updateTime)
    }

    suspend fun createDose(
        medicationId: Int,
        status: DoseStatus,
        newDosage: Int?,
        updateTime: LocalDateTime?
    ) {
        medicationDao.getMedicationById(medicationId).collect { medication ->
            val doseEntity = medication.mapToDoseEntity(
                status,
                updateTime,
                newDosage
            )
            doseDao.insertDose(doseEntity)
        }
    }
    suspend fun TakeDose(updateDoseData: UpdateDoseData) {
        withContext(Dispatchers.IO){
            if (updateDoseData.doseId != null) {
                doseDao.updateDoseStatus(updateDoseData.doseId, DoseStatus.TAKEN, LocalDateTime.now())
            }
            else {
                doseDao.insertDose(DoseEntity(
                    doseId = 0,
                    medicationId = updateDoseData.medicationId,
                    dosage = updateDoseData.dosage,
                    createdTime = LocalDateTime.now(),
                    status = DoseStatus.TAKEN

                ))
            }
        }
    }

    private val _allFlowMedications: Flow<List<MedicationEntity>> =
        medicationDao.getAllMedications()


    fun getMyMedicationsViewData(): Flow<List<MyMedicationsViewData>> {
        val medicationIdsFlow: Flow<List<Int>> = _allMedications.map { meds ->
            meds.map { it.id }
        }
        val lastTakenDoses: Flow<List<LastTakenDose>> = medicationIdsFlow.flatMapLatest { ids ->
            if (ids.isNotEmpty()) {
               doseDao.getLastTakenDosesByMedIds(ids, limit = 1)
            } else {
                flowOf(emptyList())
            }
        }
        val allMedications: Flow<List<MedicationEntity>> = _allMedications
        return combine(allMedications, lastTakenDoses){medications, doses ->
            medications.map {medication ->
                val lastDose = doses.find { it.medicationId == medication.id }
                medication.mapToMyMedicationsViewData(lastDose)
            }
        }
    }

    fun getMedicationForDisplay(medicationId: Int): Flow<MedicationEntity> =
        medicationDao.getMedicationById(medicationId)
}