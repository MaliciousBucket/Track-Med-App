package com.example.newtrackmed.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

@Composable
fun MyMedsDetailsLoadingCardIndicator(
){
    CircularProgressIndicator(
        modifier = Modifier.width(32.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        trackColor = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
fun ScreenLoadingIndicator(
){
    CircularProgressIndicator(
        modifier = Modifier.width(128.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        trackColor = MaterialTheme.colorScheme.secondary,
    )
}

@Composable
fun LoadingScreenDisplay(
    @StringRes title: Int
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
            Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
            ScreenLoadingIndicator()
        }
    }

}

@Composable
fun ErrorScreenDisplay(
    @StringRes title: Int
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        verticalArrangement = Arrangement.Center,

    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(96.dp),
                imageVector = Icons.Rounded.Error,
                contentDescription = stringResource(R.string.error_icon_description)
            )
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackMedProgressIndicators(

){
    NewTrackMedTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            ErrorScreenDisplay(title = R.string.loading_daily_doses_error)
        }
    }
}
