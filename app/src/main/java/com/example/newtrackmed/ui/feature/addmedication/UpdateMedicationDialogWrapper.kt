package com.example.newtrackmed.ui.feature.addmedication

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.feature.addmedication.questiondialog.FrequencyOption
import com.example.newtrackmed.ui.theme.NewTrackMedTheme


@Composable
fun AddMedicationDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
){
    Dialog(onDismissRequest = { onDismissRequest()})
    {
        content()
    }
}
@Composable
fun QuestionDialogWrapper(
    @StringRes title: Int,
    icon: ImageVector,
    @StringRes backButtonDescription: Int,
    errorMessage: String,
    isError: Boolean,
    onSaveClicked: (() -> Unit)?,
    onBackPressed: () -> Unit,
    content: @Composable () -> Unit,
){
        Card(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp, 0.dp, 4.dp, 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    IconButton(
                        onClick = { onBackPressed()}
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = backButtonDescription))
                    }
                    if(onSaveClicked != null){
                        TextButton(onClick = { onSaveClicked()}) {
//                            Icon(imageVector = Icons.Filled.Check, contentDescription = null)
                            Text(text = "Save")
                        }
                    }
                }
                UpdateMedDialogTitle(
                    title = title,
                    icon = icon,
                )
                DialogErrorMessage(
                    errorMessage = errorMessage,
                    isError = isError
                )
                content()
            }
        }
}
@Composable
fun UpdateMedDialogTitle(
    @StringRes title: Int,
    icon: ImageVector,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp, 4.dp, 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(48.dp)
                .align(Alignment.CenterVertically),
            imageVector = icon,
            contentDescription = null
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
    options: List<MedQuestionOption>,
    onItemClicked: (Int) -> Unit
){
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ){
        itemsIndexed(
            options,
            key = {_, item: MedQuestionOption ->
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
    option: MedQuestionOption,
    onItemClicked: () -> Unit
){
    val backgroundColor = if(option.isSelected){
        MaterialTheme.colorScheme.secondaryContainer
    }
    else {
        MaterialTheme.colorScheme.surface
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


data class MedQuestionOption(
    val text: String,
    val value: String,
    val isSelected: Boolean,
    val hasAction: Boolean
)

@Composable
fun DoseQuestionListItem(
    value: String,
    extendedName: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier,
){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if(isSelected){
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(
                isSelected,
                onClick = onOptionSelected,
                role = Role.RadioButton
            )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.padding(8.dp)){
                RadioButton(selected = isSelected, onClick = { onOptionSelected() })
            }

            Row (
                modifier = Modifier.weight(1f),
            ){
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = extendedName,
                    style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}
@Composable
fun CustomAnswerItem(
    value: String,
    isError: Boolean,
    isSelected: Boolean,
    onValueChange: (String) -> Unit,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier,
){
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            shape = MaterialTheme.shapes.small,
            color = when {
                isSelected && isError -> MaterialTheme.colorScheme.errorContainer
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            },
            border = BorderStroke(
                width = 1.dp,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.outline
                }
            ),
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .selectable(
                    isSelected,
                    onClick = onOptionSelected,
                    role = Role.RadioButton
                )
        ){
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.custom),
                style = MaterialTheme.typography.bodyMedium,
                textAlign= TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
            HorizontalDivider(modifier = Modifier
                .fillMaxWidth()
                .height(16.dp), color = Color.Red, )
            Row (
                modifier = Modifier.fillMaxWidth(),
//                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier.padding(8.dp)){
                    //TODO: Change all of these
                    RadioButton(selected = isSelected, onClick = { onOptionSelected() })
                }
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = {newValue ->
                        onValueChange(newValue)
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {keyboardController?.hide()})
                )
                if (isError){
                    Box(modifier = Modifier.padding(8.dp)){
                        Icon(
                            imageVector = Icons.Filled.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

        }
    }
}

@Composable
fun MedQuestionListOption(
    text: String,
    isSelected: Boolean,
    onOptionSelected: () -> Unit,
    modifier: Modifier = Modifier,
){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if(isSelected){
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(
                isSelected,
                onClick = onOptionSelected,
                role = Role.RadioButton
            )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.padding(8.dp)){
                RadioButton(selected = isSelected, onClick = { onOptionSelected() })
            }
            Text(
                modifier = Modifier.weight(1f),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }



    }
}

@Composable
fun MedQuestionNavOption(
    @StringRes title: Int,
    isSelected: Boolean,
    onOptionClicked: () -> Unit,
    modifier: Modifier = Modifier,
){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if(isSelected){
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(
                isSelected,
                onClick = onOptionClicked,
                role = Role.RadioButton
            )
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.padding(8.dp)){
                RadioButton(selected = isSelected, onClick = { onOptionClicked() })
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = title),
                style = MaterialTheme.typography.bodyLarge,
            )

            Box(modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = null
                )

            }
        }
    }
}

@Composable
fun DialogErrorMessage(
    errorMessage: String,
    isError: Boolean,
){
    if(isError) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.padding(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun FrequencyQuestionNavOption(
    option: FrequencyOption,
    isSelected: Boolean,
    onOptionClicked: () -> Unit,
    modifier: Modifier = Modifier,
){
    Surface(
        shape = MaterialTheme.shapes.small,
        color = if(isSelected){
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline
            }
        ),
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .selectable(
                isSelected,
                onClick = onOptionClicked,
                role = Role.RadioButton
            )
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(modifier = Modifier.padding(8.dp)){
                RadioButton(selected = isSelected, onClick = { null })
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                modifier = Modifier.weight(1f),
                text = stringResource(id = option.name),
                style = MaterialTheme.typography.bodyLarge,
            )

            Box(modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.NavigateNext,
                    contentDescription = null
                )

            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun TestScreenPreview(){
    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            UpdateMedDialogTitle(
                title = R.string.notes_and_instructions,
                icon = Icons.Filled.Edit,
            )
            DialogErrorMessage(errorMessage = "Please use numerical characters only", isError = true)


            Spacer(modifier = Modifier.height(16.dp))



            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}