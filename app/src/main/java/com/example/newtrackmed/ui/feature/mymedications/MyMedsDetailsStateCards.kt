package com.example.newtrackmed.ui.feature.mymedications

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newtrackmed.R
import com.example.newtrackmed.ui.component.MyMedsDetailsLoadingCardIndicator
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

@Composable
fun MyMedsDetailsLoadingCard(
    @StringRes title: Int
){
    OutlinedCard(
        modifier = Modifier.padding(),

        shape = RoundedCornerShape(0.dp),

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            MyMedsDetailsLoadingCardIndicator()
        }
    }
}

@Composable
fun MyMedsDetailsErrorCard(
    @StringRes title: Int
){
    OutlinedCard(
        modifier = Modifier.wrapContentHeight(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer),
        shape = RoundedCornerShape(0.dp)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Rounded.Error,
                contentDescription = stringResource(R.string.error_icon_description))
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTrackMedStateCards(

){
    NewTrackMedTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            MyMedsDetailsLoadingCard(title = R.string.loading_frequency_details)
            MyMedsDetailsErrorCard(title = R.string.loading_as_needed_error)
        }
    }
}