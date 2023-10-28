package com.example.newtrackmed.ui.feature.addmedication

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

@Composable
fun UpdateMedicationQuestionDialog(
    @StringRes title: Int,
    icon: ImageVector,
    @StringRes iconDescription: Int,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
){
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .wrapContentSize(Alignment.Center),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(

            ) {
            UpdateMedDialogTitle(
                title = title,
                icon = icon,
                iconDescription = iconDescription
            )
                content
            }
        }
    }
}
@Composable
fun UpdateMedDialogTitle(
    @StringRes title: Int,
    icon: ImageVector,
    @StringRes iconDescription: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically),
            imageVector = icon,
            contentDescription = stringResource(iconDescription)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun SelectMedicationFormContent(
    options: List<myOptions>,
    onItemClicked: (Int) -> Unit
){
    LazyColumn(
        //verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ){
        itemsIndexed(
            options,
            key = {_, item: myOptions ->
                item.hashCode()
            }
        ) { index, item ->
            AddMedListItem(option = item) {
                onItemClicked(index)
            }
            HorizontalDivider()
        }
    }
}

@Composable
fun AddMedListItem(
    option: myOptions,
    onItemClicked: () -> Unit
){
    val backgroundColor = if(option.isSelected){
        MaterialTheme.colorScheme.secondaryContainer
    }
    else {
        Color.White
    }
    ListItem(
        modifier = Modifier
            .clickable{
                onItemClicked()
            },
        colors = ListItemDefaults.colors (
            backgroundColor
        ),
        headlineContent = {
            Text(option.text) },
        trailingContent = {
            if(option.hasAction){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = null)
            }
        }
    )
}

data class myOptions(
    val text: String,
    val value: String,
    val isSelected: Boolean,
    val hasAction: Boolean
)

val myListOfOption = listOf(
    myOptions(text = "Item 1", value = "Value 1", isSelected = true, hasAction = false),
    myOptions(text = "Item 2", value = "Value 2", isSelected = false, hasAction = true),
    myOptions(text = "Item 3", value = "Value 3", isSelected = false, hasAction = false),
)

sealed class testSelection(
    title: String,

){

}



@Preview(showBackground = true)
@Composable
fun TestScreenPreview(){
    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
//            SelectMedicationFormContent(options = myListOfOption, onItemClicked = {})
            UpdateMedDialogTitle(
                title = R.string.notes_and_instructions,
                icon = Icons.Filled.Edit,
                iconDescription = R.string.edit_notes_instructions
            )


            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}