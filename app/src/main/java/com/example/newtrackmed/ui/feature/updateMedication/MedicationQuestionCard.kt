package com.example.newtrackmed.ui.feature.updateMedication

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

@Composable
fun MedicationQuestionCard(
    @StringRes titleResourceId: Int,
    iconImage: ImageVector,
){
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(0.dp),
        elevation = CardDefaults.cardElevation(
            4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp, 24.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = iconImage,
                //Purely decorative
                contentDescription = null
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(titleResourceId),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedNavCard(
    text: String,
    @StringRes placeholderResourceId: Int,
    @StringRes iconDescriptionResourceId: Int,
    onNavCardClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(0.dp),
        onClick = { onNavCardClicked() }
    ) {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
//            modifier = Modifier.padding(8.dp, 8.dp, 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavCardClicked() },
                value = text,
                onValueChange = {},
                placeholder = {
                    if (text.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = placeholderResourceId),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                readOnly = true,
                textStyle = MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
            )
            Icon(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .size(32.dp),
                imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                contentDescription = stringResource(iconDescriptionResourceId
                )
            )
        }
    }
}

@Composable
fun MedicationNavQuestion(
    @StringRes titleResourceId: Int,
    @StringRes placeholderResourceId: Int,
    @StringRes iconDescriptionResourceId: Int,
    answer: String,
    iconImage: ImageVector,
    onNavClicked: () -> Unit

){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        MedicationQuestionCard(
            titleResourceId =titleResourceId,
            iconImage = iconImage
        )
        AddMedNavCard(
            text = answer,
            placeholderResourceId = placeholderResourceId,
            iconDescriptionResourceId = iconDescriptionResourceId) {
            onNavClicked()
        }
    }
}

@Composable
fun AddMedicationTextQuestion(
    @StringRes titleResourceId: Int,
    @StringRes placeholderResourceId: Int,
    input: String,
    iconImage: ImageVector,
    keyboardType: KeyboardType,
    isInputError: Boolean,
    errorMessage: String,
    onValueChange: (String) -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        MedicationQuestionCard(
            titleResourceId =titleResourceId,
            iconImage = iconImage
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(0.dp),
        ) {
            TextField(
//                modifier = Modifier.weight(1f),
                value = input,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                onValueChange = { newValue ->
                    onValueChange(newValue)
                },
                placeholder = {
                    if (input.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = placeholderResourceId),
//                            textAlign = TextAlign.Center
                        )
                    }
                },
                textStyle = MaterialTheme.typography.titleMedium,
//                    .copy(textAlign = TextAlign.Center),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                ),
                isError = isInputError,
                supportingText = {
                    if (isInputError){
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error)
                    }
                },
                trailingIcon = {
                    if(isInputError){
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                }
            )
        }

    }
}

@Composable
fun AddMedSaveButton(
    @StringRes text: Int,
    isError: Boolean,
    onClick: () -> Unit
){
    val normalColor = Color(0xFF6DD58C)
    val errorColor = MaterialTheme.colorScheme.errorContainer
    val buttonColor = if (isError) errorColor else normalColor

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = { onClick() }) {
        Text(text = stringResource(text))
    }
}
@Composable
fun AddMedDialogSaveButton(
    @StringRes text: Int,
    onClick: () -> Unit
){
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF6DD58C)
        ),
        shape = RoundedCornerShape(0.dp),
        onClick = { onClick() }) {
        Text(text = stringResource(text))

    }
}


@Preview(showBackground = true)
@Composable
fun MedicationQuestionCardPreview(){
    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MedicationQuestionCard(titleResourceId = R.string.add_medication_name, iconImage = Icons.Filled.Edit)


            AddMedNavCard(
                text = "",
                placeholderResourceId = R.string.add_med_type_placeholder,
                iconDescriptionResourceId = R.string.navigate_to_select_med_type,
                onNavCardClicked = {}
            )

            AddMedSaveButton(text = R.string.save_medication_details, isError = true) {
                
            }
        }
    }
}