package com.example.newtrackmed.ui.feature.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf

@Composable
fun TestBarChat(){
    val chartEntryModelProducer1 = ChartEntryModelProducer(entriesOf(4f, 12f, 8f, 16f))
    val chartEntryModelProducer2 = ChartEntryModelProducer(entriesOf(16f, 8f, 12f, 4f))
    val composedChartEntryModelProducer = chartEntryModelProducer1 + chartEntryModelProducer2

    val columnChart = columnChart()
    val lineChart = lineChart()
    Chart(
        chart = remember(columnChart, lineChart) { columnChart + lineChart },
        chartModelProducer = composedChartEntryModelProducer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis(),
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharts(){
    TestBarChat()
}