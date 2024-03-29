package com.example.newtrackmed.ui.feature.report

import android.graphics.Typeface
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.LegendLabel
import co.yml.charts.common.model.LegendsConfig
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.utils.proportion
import com.example.newtrackmed.ui.theme.NewTrackMedTheme

@Composable
fun testDonut() {


    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("HP", 15f, Color(0xFF5F0A87)),
            PieChartData.Slice("Dell", 30f, Color(0xFF20BF55)),
            PieChartData.Slice("Lenovo", 10f, Color(0xFFA40606)),
            PieChartData.Slice("Asus", 15f, Color(0xFFF53844)),
            PieChartData.Slice("Acer", 10f, Color(0xFFEC9F05)),
            PieChartData.Slice("Apple", 30f, Color(0xFF009FFD)),
        ),
        plotType = PlotType.Donut
    )
    
    val sumOfValues = donutChartData.totalLength
    val proportions = donutChartData.slices.proportion(sumOfValues)


    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            strokeWidth = 120f,
            labelColor = Color.Black,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
            isAnimationEnable = true,
            chartPadding = 25,
            labelFontSize = 42.sp,
        )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ){
        Legends(legendsConfig = DataUtils.getLegendsConfigFromPieChartData(pieChartData = donutChartData, 3))
        DonutPieChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            pieChartData = donutChartData,
            pieChartConfig = pieChartConfig
        ){
            slice -> 

        }
    }
}

@Composable
fun NewDonut(
    pieChartData: PieChartData?,
    modifier: Modifier = Modifier
){

    if(pieChartData != null){
        val sumOfValues = pieChartData.totalLength

        val proportions = pieChartData.slices.proportion(sumOfValues)

        val pieChartConfig =
            PieChartConfig(
                labelVisible = true,
                strokeWidth = 55f,
                labelColor = Color.Black,
                activeSliceAlpha = .9f,
                isEllipsizeEnabled = true,
                labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
                isAnimationEnable = true,
                chartPadding = 60,
                labelFontSize = 42.sp,
            )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()

        ){


            DonutPieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
//                    .padding(8.dp),
                pieChartData = pieChartData!!,
                pieChartConfig = pieChartConfig
            ){
                    slice ->

            }
            Legends(legendsConfig = getDonutLegend(pieChartData = pieChartData, gridSize = 2, customTextSize = 14.sp))
            Text(text = "Total Doses: ${pieChartData.totalLength.toInt()}")

        }



    } else {
        Text(text = "No Data :(")
    }

}

fun getDonutLegend(
    pieChartData: PieChartData,
    gridSize: Int,
    customTextSize: TextUnit
): LegendsConfig {
    val legendsList = mutableListOf<LegendLabel>()
    pieChartData.slices.forEach { slice ->
        legendsList.add(LegendLabel(slice.color, slice.label))
    }
    return LegendsConfig(
        legendLabelList = legendsList,
        textSize = customTextSize,
        gridColumnCount = gridSize,
        gridPaddingHorizontal = 8.dp,
        gridPaddingVertical = 2.dp,
        colorBoxSize = 6.dp,
        textStyle = TextStyle(
            fontSize = customTextSize
        ),
        spaceBWLabelAndColorBox = 4.dp,
        legendsArrangement = Arrangement.Start
    )
}

@Composable
fun NewestDonut(
    pieChartData: PieChartData?,
    showChart: Boolean,
    onFetchData: () -> Unit
){
    if(showChart) {


        if (pieChartData != null) {
            val sumOfValues = pieChartData.totalLength

            val proportions = pieChartData.slices.proportion(sumOfValues)

            val pieChartConfig =
                PieChartConfig(
                    labelVisible = true,
                    strokeWidth = 120f,
                    labelColor = Color.Black,
                    activeSliceAlpha = .9f,
                    isEllipsizeEnabled = true,
                    labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
                    isAnimationEnable = true,
                    chartPadding = 25,
                    labelFontSize = 42.sp,
                )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                Legends(
                    legendsConfig = DataUtils.getLegendsConfigFromPieChartData(
                        pieChartData = pieChartData!!,
                        3
                    )
                )
                DonutPieChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    pieChartData = pieChartData!!,
                    pieChartConfig = pieChartConfig
                ) { slice ->

                }
            }


        } else {
            Text(text = "No Data :(")
        }
    } else {
        Text(text = "${showChart}, Chart not showing")
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewDonutChart(
    
){
    NewTrackMedTheme {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            testDonut()
        }
    }
}