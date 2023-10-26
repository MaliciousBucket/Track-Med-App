package com.example.newtrackmed.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.newtrackmed.R
import com.example.newtrackmed.data.doseViewDataList
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.MyMedicationsViewData
import com.example.newtrackmed.data.model.UpdateDoseActions
import com.example.newtrackmed.data.model.UpdateDoseData
import com.example.newtrackmed.data.myMedicationsPreviewData
import com.example.newtrackmed.data.previewUpdateDoseData
import com.example.newtrackmed.ui.feature.home.DisplayDoseCards
import com.example.newtrackmed.ui.feature.home.DoseCardMedDetails
import com.example.newtrackmed.ui.feature.home.HomeTopAppBar
import com.example.newtrackmed.ui.feature.home.LastDoseChip
import com.example.newtrackmed.ui.feature.home.TestUpdateDoseDialog
import com.example.newtrackmed.ui.feature.mymedications.DisplayMyMedicationsList
import com.example.newtrackmed.ui.theme.NewTrackMedTheme
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun TestScreenPreview(){
    val testUpdatedta = previewUpdateDoseData[0]
val testData = doseViewDataList



NewTrackMedTheme {
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))



        Spacer(modifier = Modifier.height(16.dp))


//        DisplayMyMedicationsList(
//            myMedicationViewDataList = myMedicationsPreviewData,
//            onListItemClick = {}
//        )


        Spacer(modifier = Modifier.height(16.dp))


//        }


        }
    }
}








@Composable
fun TestScreen(){

}














