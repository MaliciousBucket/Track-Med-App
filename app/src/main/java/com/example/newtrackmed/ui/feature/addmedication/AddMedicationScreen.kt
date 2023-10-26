package com.example.newtrackmed.ui.feature.addmedication

import androidx.annotation.StringRes
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R

@Composable
fun AddMedicationScreen(){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationNavToDialogQuestion(
    icon: ImageVector,
    @StringRes title: Int,
    @StringRes navigateText: Int,
    onEnterFormClicked: () -> Unit,

    ){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AddMedicationCard(
                icon = icon,
                title = title
            )
            NavigateToDialogCard(text = navigateText) {
                onEnterFormClicked()
            }
        }
    }
}

@Composable
fun AddMedicationQuestion(
    icon: ImageVector,
    @StringRes title: Int,
    content: @Composable () -> Unit,


    ){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AddMedicationCard(
                icon = icon,
                title = title
            )
            content()
        }
    }
}
//https://stackoverflow.com/questions/69048715/is-there-a-way-in-jetpack-compose-to-retreive-the-real-string-value-of-a-string
//https://stackoverflow.com/questions/74458452/jetpack-compose-handling-both-a-string-and-string-resource-identifier-for-a-text
@Composable
fun AddMedFormQuestion(
    onSelectFormClicked: () -> Unit
){
    AddMedicationQuestion(
        icon = Icons.Filled.Medication,
        title = R.string.enter_med_form,
    ) {
        NavigateToDialogCard(
            text = (R.string.enter_med_form_placeholder),
            onClick = onSelectFormClicked
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationCard(
    icon: ImageVector,
    @StringRes title: Int,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = Modifier
            .wrapContentSize(),
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(0.dp),
    ){
        Column(
            modifier = Modifier.wrapContentSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(title),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun NavigateToDialogCard(
    @StringRes text: Int,
    onClick: () -> Unit
){
    OutlinedCard(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
    ) {
        Row (
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(text),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.AutoMirrored.Filled.NavigateNext, contentDescription = null)
        }
    }
}
