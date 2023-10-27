package com.example.newtrackmed.ui.feature.home

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Redo
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.newtrackmed.R
import com.example.newtrackmed.data.model.LastTakenDose
import com.example.newtrackmed.data.model.UpdateDoseActions
import com.example.newtrackmed.data.model.UpdateDoseData
import java.time.format.DateTimeFormatter

@Composable
fun UpdateDoseDialog(
    updateDoseData: UpdateDoseData,
    onDismissRequest: () -> Unit,
    onTakeClick: () -> Unit,
    onUnTakeClick: () -> Unit,
    onSkipClick: () -> Unit,
    onMissedClick: () -> Unit,
    onCancelClick: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest() }) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentSize(Alignment.Center),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.wrapContentSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.Medication, contentDescription = null)
                DoseCardMedDetails(
                    name = updateDoseData.name,
                    dosage = updateDoseData.dosage,
                    dosageUnit = updateDoseData.dosageUnit,
                    unitsTaken = updateDoseData.unitsTaken,
                    type = updateDoseData.type)

                Spacer(modifier = Modifier.height(4.dp))

                LastDoseChip(
                    lastDose = updateDoseData.lastTakenDose,
                    type = updateDoseData.type
                )

                DisplayMedNotesAndInstructions(
                    notes = updateDoseData.notes,
                    instructions = updateDoseData.instructions)

                DisplayUpdateDoseDialogActions(
                    actions = updateDoseData.updateDoseActions,
                    onTakeClick = { onTakeClick() },
                    onUnTakeClick = { onUnTakeClick() },
                    onSkipClick = { onSkipClick() },
                    onMissedClick = { onMissedClick() },
                    onCancelClick = { onCancelClick() },
                    onEditClick = { onEditClick() }) {

                }

            }
        }
    }
}

//TODO: Implement Edit

@Composable
fun DisplayUpdateDoseDialogActions(
    actions: UpdateDoseActions,
    onTakeClick: () -> Unit,
    onUnTakeClick: () -> Unit,
    onSkipClick: () -> Unit,
    onMissedClick: () -> Unit,
    onCancelClick: () -> Unit,
    onEditClick: () -> Unit,
    onCancelEditClick: () -> Unit

){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.wrapContentSize()
    ) {
        CancelIconButton {
            onCancelClick()
        }

        when (actions) {
            UpdateDoseActions.TAKE -> {
                SkipIconButton {
                    onSkipClick()
                }
                TakeIconButton {
                    onTakeClick()
                }
            }

            UpdateDoseActions.TAKEN -> {
                SkipIconButton {
                    onSkipClick()
                }
                UnTakeIconButton {
                    onUnTakeClick()
                }
            }

            UpdateDoseActions.SKIPPED -> {
                MissedIconButton {
                    onMissedClick()
                }
                TakeIconButton {
                    onTakeClick()
                }
            }

            UpdateDoseActions.MISSED -> {
                SkipIconButton {
                    onSkipClick()
                }
                TakeIconButton {
                    onTakeClick()
                }
            }

            UpdateDoseActions.RESCHEDULED -> {

            }
        }
    }


}

@Composable
fun IconButtonWithText(
    icon: ImageVector,
    @StringRes text: Int,
    onClick: () -> Unit
) {
    TextButton(onClick = {
        onClick()
    }) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(text = stringResource(id = text), maxLines = 2)
        }
    }
}

@Composable
fun TakeIconButton(
    onTakeClick: () -> Unit){
    IconButtonWithText(
        icon = Icons.Default.Done,
        text = R.string.take,
        onClick = {onTakeClick()}
    )
}

@Composable
fun UnTakeIconButton(onUnTakeClick: () -> Unit){
    IconButtonWithText(
        icon = Icons.Default.Close,
        text = R.string.un_take,
        onClick = {onUnTakeClick()}
    )
}

@Composable
fun SkipIconButton(onSkipClick: () -> Unit){
    IconButtonWithText(icon = Icons.Default.Redo,
        text = R.string.skip,
        onClick = {  onSkipClick()})
}

@Composable
fun MissedIconButton(onMissedClick: () -> Unit){
    IconButtonWithText(icon = Icons.Filled.PriorityHigh,
        text = R.string.missed,
        onClick = {  onMissedClick()})
}

@Composable
fun CancelIconButton(onCancelClick: () -> Unit){
    IconButtonWithText(
        icon = Icons.Default.Cancel,
        text = R.string.cancel,
        onClick = {
            onCancelClick()
        }
    )
}


@Composable
fun EditIconButton(onEditClick: () -> Unit){
    IconButtonWithText(
        icon = Icons.Default.Edit,
        text = R.string.edit,
        onClick = {  onEditClick()})
}

@Composable
fun CanelEditIconButton(onCancelEditClick: () -> Unit){
    IconButtonWithText(
        icon = Icons.Default.EditOff,
        text = R.string.cancel,
        onClick = {onCancelEditClick() })
}





@Composable
fun LastDoseChip(
    lastDose: LastTakenDose?,
    type: String
){
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")
    val formattedTime = lastDose?.createdTime?.format(formatter)
    val chipLabel = if(lastDose != null){
        "${lastDose.dosage} $type(s) Taken: $formattedTime"
    }
    else {
        "No doses recorded."
    }
    SuggestionChip(
        onClick = { /* Handle click */ },
        label = { Text(chipLabel) }
    )
}

@Composable
fun ExpandableCard(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = "Expand/Collapse Arrow",
                    modifier = Modifier
                        .padding(4.dp)
                        .graphicsLayer(rotationZ = if (expanded) 90f else 0f)
                )
            }
            if (expanded) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
fun DisplayMedNotesAndInstructions(
    notes: String?,
    instructions: String?,
){
    if(notes != null || instructions != null) {
        Column(
            modifier = Modifier.wrapContentSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (instructions != null) {
                ExpandableCard(title = "Instructions", content = instructions)
            }
            if (notes != null) {
                ExpandableCard(title = "Notes", content = notes)
            }
        }
    }
}