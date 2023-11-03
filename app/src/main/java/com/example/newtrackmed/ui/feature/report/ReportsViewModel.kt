package com.example.newtrackmed.ui.feature.report

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.newtrackmed.data.entity.DoseStatus
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.RecentDoseDetails
import com.example.newtrackmed.data.repository.DoseRepository
import com.example.newtrackmed.data.repository.MedicationRepository
import com.example.newtrackmed.di.TrackMedApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ReportsUiState(
    val donutChartStatus: ChartStatus,
    val mostRecentDoses: MostRecentDoses,
    val recentDosesWithDetails: RecentDosesWithDetails

)
@Immutable
sealed interface ChartStatus{
    object Donut : ChartStatus

    object BarGraph: ChartStatus

    object LineGraph: ChartStatus

    object Empty: ChartStatus

    object Loading : ChartStatus

    object Error : ChartStatus
}

@Immutable
sealed interface RecentDosesWithDetails{
    data class Success(val recentDoses: List<RecentDoseDetails?>) :
        RecentDosesWithDetails
    object Loading : RecentDosesWithDetails
}

@Immutable
sealed interface MostRecentDoses{
    data class ForSingleMed(val mostRecentDoses: List<LastTakenDose?>) : MostRecentDoses
    data class ForMultipleMeds(val mostRecentDoses: List<LastTakenDose?>): MostRecentDoses
    object Loading: MostRecentDoses
}

class ReportsViewModel(
    private val doseRepository: DoseRepository,
    private val medicationRepository: MedicationRepository
): ViewModel() {

    private val _singleMedDisplayId = MutableStateFlow<Int>(0)

    private val _multipleMedDisplayIds = MutableStateFlow<List<Int>>(emptyList())

    private val _allDoseDonutChartData = MutableStateFlow<PieChartData?>(null)
    val allDoseDonutChartData = _allDoseDonutChartData.asStateFlow()

    private val _doseDonutChartDataForSelectedMed = MutableStateFlow<PieChartData?>(null)


    private val _doseDonutChartDataForMultipleMeds = MutableStateFlow<PieChartData?>(null)

    private val _recentDosesForSelectedMed = MutableStateFlow<List<LastTakenDose?>>(emptyList())
    private val _recentDosesForMultipleMeds = MutableStateFlow<List<LastTakenDose?>>(emptyList())

    private val _recentDoseDetailsForSelectedMedId = MutableStateFlow<List<RecentDoseDetails?>>(
        emptyList())
    private val _recentDoseDetailsForMultipleMeds = MutableStateFlow<List<RecentDoseDetails?>>(
        emptyList())

    private val _mostRecentDosesData = MutableStateFlow<MostRecentDoses>(MostRecentDoses.Loading)
    private val _recentDosesWithDetails = MutableStateFlow<RecentDosesWithDetails>(RecentDosesWithDetails.Loading)
    private val _donutChartData = MutableStateFlow<ChartStatus>(ChartStatus.Loading)

    private val _showChartData = mutableStateOf(false)
    val showChartData: Boolean
        get() = _showChartData.value


    private val statusColours = mapOf(
        DoseStatus.TAKEN to Color(0xFF00FF00),
        DoseStatus.MISSED to Color(0xFFFF0000),
        DoseStatus.SKIPPED to Color(0xFFFFFF00),
        DoseStatus.RESCHEDULED to Color(0xFFFFA500)
    )

    val uiState: StateFlow<ReportsUiState> = combine(
        _mostRecentDosesData,
        _recentDosesWithDetails,
        _donutChartData
    ) { mostRecentDoses, recentDosesWithDetails, donutChartData ->
        ReportsUiState(
            donutChartData,
            mostRecentDoses,
            recentDosesWithDetails
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ReportsUiState(
            ChartStatus.Loading,
            MostRecentDoses.Loading,
            RecentDosesWithDetails.Loading
        )
    )


    fun fetchAllDoseDonutChartData(){
        Log.d("Debugging reports", "Fetching all dose donut data")
        viewModelScope.launch {
            doseRepository.getDoseCounts().first().let {doseCounts ->
                Log.d("Debug test Donut", "DoseCounts: $doseCounts" )
                val slices = doseCounts.map { doseCount ->
                    val colour = statusColours[doseCount.status] ?: Color.Black
                    PieChartData.Slice(
                        doseCount.status.name,
                        doseCount.count.toFloat(),
                        colour)
                }

                val newPieChartData = PieChartData(
                    slices = slices,
                    plotType = PlotType.Donut
                )
                _allDoseDonutChartData.update { newPieChartData }
            }
        }
    }

    fun showTestChart(){
        Log.d("Debugging reports", "Show test chart entered")

        viewModelScope.launch {
            _donutChartData.update { ChartStatus.Loading }
            fetchAllDoseDonutChartData()
            _allDoseDonutChartData.value?.let { donutData ->
                _donutChartData.update { ChartStatus.Donut }
                Log.d("Debug Status", "Value: ${_donutChartData.value}")
            } ?: _donutChartData.update { ChartStatus.Empty }
        }
    }


    fun donutForId(id: Int){
        Log.d("Debugging reports", "Donut For Id entered")

        viewModelScope.launch{
            _showChartData.value = false
            _singleMedDisplayId.value = id
            fetchDonutChartDataForSingleMed(id)
            Log.d("Selected Id", "ID: ${_singleMedDisplayId.value}")
            Log.d("Selected Id", "ID: ${_allDoseDonutChartData.value}")
            if(_allDoseDonutChartData.isNotNull()) {
                _showChartData.value = true
            }
//            _donutChartData.update { ChartStatus.Loading }
//            fetchDonutChartDataForSingleMed(id)
//            _doseDonutChartDataForSelectedMed.value?.let { donutData ->
//                Log.d("Debug donut for ID", "Data: $donutData")
//                _allDoseDonutChartData.update { donutData }
//                _donutChartData.update { ChartStatus.Donut }
//            } ?: _donutChartData.update { ChartStatus.Empty }

        }

    }

    fun recentDetailDosesForMed(){
        Log.d("Debugging reports", "Recent Details Entered")

        viewModelScope.launch{
            _recentDosesWithDetails.update { RecentDosesWithDetails.Loading }
            fetchRecentDosesDetailsForSingleMed(0, 10)
            _recentDoseDetailsForSelectedMedId.value?.let { doseData ->
                _recentDosesWithDetails.update { RecentDosesWithDetails.Success(_recentDoseDetailsForSelectedMedId.value) }
            }
        }
    }
//    _recentDoseDetailsForMultipleMeds
    fun recentDetailDosesForMultipleMeds(){
    Log.d("Debugging reports", "Recent details multople entered")

    viewModelScope.launch{
            fetchRecentDoseDetailsForMultipleMeds(listOf(0, 1), 10)
            _recentDosesWithDetails.update { RecentDosesWithDetails.Loading }
            _recentDoseDetailsForMultipleMeds.value?.let { doseData ->
                _recentDosesWithDetails.update { RecentDosesWithDetails.Success(_recentDoseDetailsForSelectedMedId.value) }
            }
        }
    }

    fun fetchDonutChartDataForSingleMed(medicationId: Int) {
        viewModelScope.launch {
            _singleMedDisplayId
                .flatMapLatest { medicationId ->
                    Log.d("Getting Donut Data", "For: $medicationId")
                    doseRepository.getDoseCountsByMedId(medicationId)
                }.distinctUntilChanged()
                .collect { doseCounts ->
                    val slices = doseCounts.map { doseCount ->
                        val colour = statusColours[doseCount.status] ?: Color.Black
                        PieChartData.Slice(
                            doseCount.status.name,
                            doseCount.count.toFloat(),
                            colour
                        )
                    }

                    val newPieChartData = PieChartData(
                        slices = slices,
                        plotType = PlotType.Donut
                    )
//                    _doseDonutChartDataForSelectedMed.update { newPieChartData }
                    _allDoseDonutChartData.update { newPieChartData }
                }
        }
    }

    fun fetchDonutChartDataForMultipleMeds(medicationIds: List<Int>) {
        viewModelScope.launch {
            doseRepository.getDoseCountsByMultipleMedIds(medicationIds).distinctUntilChanged()
                .collect { doseCounts ->
                val aggregatedCounts = doseCounts.groupBy { it.status }
                    .mapValues { (_, list) -> list.sumOf { it.count } }

                val slices = aggregatedCounts.map { (status, count) ->
                    val colour = statusColours[status] ?: Color.Black
                    PieChartData.Slice(
                        status.name,
                        count.toFloat(),
                        colour
                    )
                }

                val newPieChartData = PieChartData(
                    slices = slices,
                    plotType = PlotType.Donut
                )
                _doseDonutChartDataForMultipleMeds.update { newPieChartData }
            }
        }
    }

    fun fetchLimitedDonutChartDataForSingleMed(medicationId: Int, limit: Int) {
        viewModelScope.launch {
            _singleMedDisplayId
                .flatMapLatest { medicationId ->
                    doseRepository.getDoseCountsByMedIdWithLimit(medicationId, limit)
                }.distinctUntilChanged()
                .collect { doseCounts ->
                    val slices = doseCounts.map { doseCount ->
                        val colour = statusColours[doseCount.status] ?: Color.Black
                        PieChartData.Slice(
                            doseCount.status.name,
                            doseCount.count.toFloat(),
                            colour
                        )
                    }

                    val newPieChartData = PieChartData(
                        slices = slices,
                        plotType = PlotType.Donut
                    )
                    _doseDonutChartDataForSelectedMed.update { newPieChartData }
                }
        }
    }

    fun fetchLimitedDonutChartDataForMultipleMeds(medicationIds: List<Int>, limit: Int) {
        viewModelScope.launch {
            doseRepository.getDoseCountsByMultipleMedIdsWithLimit(medicationIds, limit).distinctUntilChanged()
                .collect { doseCounts ->
                    val aggregatedCounts = doseCounts.groupBy { it.status }
                        .mapValues { (_, list) -> list.sumOf { it.count } }

                    val slices = aggregatedCounts.map { (status, count) ->
                        val colour = statusColours[status] ?: Color.Black
                        PieChartData.Slice(
                            status.name,
                            count.toFloat(),
                            colour
                        )
                    }

                    val newPieChartData = PieChartData(
                        slices = slices,
                        plotType = PlotType.Donut
                    )
                    _doseDonutChartDataForMultipleMeds.update { newPieChartData }
                }
        }
    }





// ----- Recent Doses -----


    fun fetchRecentDosesForSingleMed(medicationId: Int, limit: Int){
        viewModelScope.launch{
            _singleMedDisplayId
                .flatMapLatest { medicationId ->
                    doseRepository.getLastTakenDoseByMedicationIds(listOf(medicationId), limit)
                }.distinctUntilChanged()
                .collect() {recentDoses ->
                    _recentDosesForSelectedMed.update { recentDoses }
                }
        }

    }

    fun fetchRecentDosesForMultipleMeds(medicationIds: List<Int>, limit: Int) {
        viewModelScope.launch {
            doseRepository.getLastTakenDoseByMedicationIds(medicationIds, limit)
                .distinctUntilChanged()
                .collect { recentDoses ->
                    _recentDosesForSelectedMed.update { recentDoses }
                }
        }
    }

    fun fetchRecentDosesDetailsForSingleMed(medicationId: Int, limit: Int) {
        viewModelScope.launch {
            val doseWithHistoryList = doseRepository.getDosesWithHistoryForMedId(medicationId, limit).first()
            val medication = medicationRepository.getMedicationById(medicationId).first()

            val recentDoses = doseWithHistoryList.map { doseWithHistory ->
                RecentDoseDetails(doseWithHistory, medication)
            }

            _recentDoseDetailsForSelectedMedId.update { recentDoses }
        }
    }

    fun fetchRecentDoseDetailsForMultipleMeds(medicationIds: List<Int>, limit: Int) {
        viewModelScope.launch {
            val allRecentDoses = mutableListOf<RecentDoseDetails>()

            for (medicationId in medicationIds) {
                val doseWithHistoryList = doseRepository.getDosesWithHistoryForMedId(medicationId, limit).first()
                val medication = medicationRepository.getMedicationById(medicationId).first()

                val recentDoses = doseWithHistoryList.map { doseWithHistory ->
                    RecentDoseDetails(doseWithHistory, medication)
                }
                allRecentDoses.addAll(recentDoses)
            }

            val sortedAndLimitedDoses = allRecentDoses.sortedByDescending { it.doseTime }.take(limit)

            _recentDoseDetailsForMultipleMeds.update { sortedAndLimitedDoses }
        }
    }





    fun fetchDosesForLastWeekForSingleMed(medicationId: Int){

    }

    fun fetchDosesForLastWeekForMultipleMeds(medicationIds: List<Int>){

    }

    fun fetchDosesForLastMonthForSingleMed(medicationId: Int){

    }



    fun fetchDosesForLastMonthForMultipleMeds(medicationIds: List<Int>){

    }




    companion object{
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appModule = TrackMedApp.appModule
                val doseRepository = appModule.doseRepository
                val medicationRepository = appModule.medicationRepository

                ReportsViewModel(
                    doseRepository = doseRepository,
                    medicationRepository = medicationRepository
                )

            }
        }
    }



}