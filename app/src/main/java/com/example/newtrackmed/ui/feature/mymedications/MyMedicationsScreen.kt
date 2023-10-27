package com.example.newtrackmed.ui.feature.mymedications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.newtrackmed.R
import com.example.newtrackmed.data.model.MyMedicationsViewData
import com.example.newtrackmed.ui.component.BottomNavBar
import com.example.newtrackmed.ui.feature.home.DoseCardMedDetails
import com.example.newtrackmed.ui.feature.home.LastDoseChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMedicationsScreen(){
    val myMedsViewModel: MyMedicationsViewModel = viewModel(
        factory = MyMedicationsViewModel.Factory
    )

    val uiState by myMedsViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
//            MyMedicationsTopAppBar(
//                title = ,
//                isMainScreen = ,
//                menuExpanded = ,
//                showAddButton = ,
//                canNavigateBack = ,
//                onAddButtonClicked = { /*TODO*/ },
//                onNavigateBackPressed = { /*TODO*/ },
//                onAddMedicationClicked = { /*TODO*/ },
//                onAddDoseClicked = { /*TODO*/ },
//                onAddDoseForMedClicked = { /*TODO*/ },
//                onDropDownMenuDismissRequest = { }
//            )
                 CenterAlignedTopAppBar(title = { Text(text = "MyMedications") })
        },
        bottomBar = {
            BottomNavBar(
                onHomeClick = { /*TODO*/ },
                onMyMedicationsClick = { /*TODO*/ },
                onReportsClick = {}
            )
        },

    )
    {innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ){
            when (uiState.myMedicationsScreen) {
                is MyMedicationsScreenUiState.Overview -> {
                    MyMedicationsOverviewScreen(
                        onListItemClicked = {},
                        uiState = uiState.myMedicationsOverview
                    )
                }
                is MyMedicationsScreenUiState.Details -> {
                    Text(text = "Well, We're at the details")
                }
            }
        }
    }



}

@Composable
fun MyMedicationsOverviewScreen(
    modifier: Modifier = Modifier,
    onListItemClicked: (Int) -> Unit,
    uiState: MyMedicationsOverviewUiState
){
    when (uiState) {
        is MyMedicationsOverviewUiState.Success -> {
            if (uiState.myMedicationsViewData.isEmpty()){
                Text(text = "Emppty List!")
            } else {
                DisplayMyMedicationsList(
                    myMedicationViewDataList = uiState.myMedicationsViewData,
                    onListItemClick = onListItemClicked)
            }
        }
        is MyMedicationsOverviewUiState.Loading -> {
            Text(text = "I should put a loading indicator here....")
        }
        is MyMedicationsOverviewUiState.Error -> {
            Text(text = "I need an error screen...")
        }
    }

}

@Composable
fun DisplayMyMedicationsList(
    myMedicationViewDataList: List<MyMedicationsViewData>,
    onListItemClick: (Int) -> Unit
){
    val (activeItems, inactiveItems) = myMedicationViewDataList.partition { it.isActive }
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(activeItems) { _, item ->
            MyMedicationListItem(viewData = item) {
                onListItemClick(item.medicationId)
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        itemsIndexed(inactiveItems) { _, item ->
            MyMedicationListItem(viewData = item) {
                onListItemClick(item.medicationId)
            }
        }
    }
}

@Composable
fun MyMedicationListItem(
    viewData: MyMedicationsViewData,
    onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        leadingContent = {
        },
        headlineContent = {
            DoseCardMedDetails(
                name = viewData.name,
                dosage = viewData.dosage,
                dosageUnit = viewData.dosageUnit,
                unitsTaken = viewData.unitsTaken,
                type = viewData.type)
        },
        supportingContent = {
            LastDoseChip(
                lastDose = viewData.lastTakenDose,
                type = viewData.type)
        },
        trailingContent = {
            IconButton(onClick = { onClick() }) {
                Icon(
                    Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = "Localized description"
                )
            }

        }
    )
    HorizontalDivider()
}


//TODO: Change to work with index
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMedicationsTopAppBar(
    title: String,
    isMainScreen: Boolean,
    menuExpanded: Boolean,
    showAddButton: Boolean,
    canNavigateBack: Boolean,
    onAddButtonClicked: () -> Unit,
    onNavigateBackPressed: () -> Unit,
    onAddMedicationClicked: () -> Unit,
    onAddDoseClicked: () -> Unit,
    onAddDoseForMedClicked: () -> Unit,
    onDropDownMenuDismissRequest: () -> Unit,
){
    CenterAlignedTopAppBar(
        title = {
            Text(title)
        },
        actions = {
            if(showAddButton){
                IconButton(
                    onClick = { onAddButtonClicked() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_dose)
                    )
                }
                MyMedicationsDropDownMenu(
                    menuExpanded = menuExpanded,
                    isMainScreen = isMainScreen,
                    onAddMedClicked = {  onAddMedicationClicked() },
                    onAddDoseClicked = { onAddDoseClicked() },
                    onAddDoseForMedClicked = { onAddDoseForMedClicked()},
                    onDropDownMenuDismissRequest = { onDropDownMenuDismissRequest() }
                )
            }
        },
        navigationIcon = {
            if(canNavigateBack) {
                IconButton(
                    onClick = { onNavigateBackPressed() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.navigate_back)
                    )
                }
            }
        }
    )
}

@Composable
fun MyMedicationsDropDownMenu(
    menuExpanded: Boolean,
    isMainScreen: Boolean,
    onAddMedClicked: () -> Unit,
    onAddDoseClicked: () -> Unit,
    onAddDoseForMedClicked: () -> Unit,
    onDropDownMenuDismissRequest: () -> Unit,

){
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { onDropDownMenuDismissRequest() }
    ) {
        if(isMainScreen) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.new_medication)) },
                onClick = { onAddMedClicked() },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = stringResource(R.string.add_medication)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.new_dose)) },
                onClick = { onAddDoseClicked() },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = stringResource(R.string.add_dose)
                    )
                }
            )
        } else {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.add_dose_for_this_med)) },
                onClick = { onAddDoseForMedClicked() },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Medication,
                        contentDescription = stringResource(R.string.add_dose)
                    )
                }
            )
        }

    }
}