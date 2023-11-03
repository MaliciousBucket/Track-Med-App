package com.example.newtrackmed.ui.feature.report

import android.graphics.Typeface
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.composed.plus
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.entry.entriesOf



import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newtrackmed.R

import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.overlayingComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.horizontalLegend
import com.patrykandpatrick.vico.compose.legend.legendItem
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.style.LocalChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.DefaultColors
import com.patrykandpatrick.vico.core.DefaultDimens
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer

import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.dimensions.HorizontalDimensions
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.core.legend.HorizontalLegend
import com.patrykandpatrick.vico.core.legend.VerticalLegend
import com.patrykandpatrick.vico.core.marker.Marker

private val model = entryModelOf(1, 2, 3, 4)

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
@Composable
fun TestDoseBarChart(
    modelProducer: ChartEntryModelProducer
){
    Chart(
        chart = columnChart(),
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(),
        bottomAxis = rememberBottomAxis()
    )

}

private val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
private val bottomAxisValueFormatter =
    AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ -> daysOfWeek[x.toInt() % daysOfWeek.size] }

val _displayMedNames = listOf<String>("Ibuprofen, Panadol, Mesalazine, testaphrine, morphine, Imuran")

val  _bottomAxisValueFormatter =
    AxisValueFormatter<AxisPosition.Horizontal.Bottom>
    {
            x, _ -> _displayMedNames[x.toInt() % _displayMedNames.size]
    }

val entriesList0 = listOf<ChartEntry>(
    entryOf(0f, 6f),
    entryOf(1f, 7f,),
    entryOf(2f, 8f),
    entryOf(3f, 2f)
)

val entriesList1 = listOf<ChartEntry>(
    entryOf(0f, 6f),
    entryOf(1f, 7f,),
    entryOf(2f, 8f),
    entryOf(3f, 2f)
)

val entriesList2 = listOf<ChartEntry>(
    entryOf(0f, 6f),
    entryOf(1f, 7f,),
    entryOf(2f, 8f),
    entryOf(3f, 2f)
)

val entriesList3 = listOf<ChartEntry>(
    entryOf(0f, 6f),
    entryOf(1f, 7f,),
    entryOf(2f, 8f),
    entryOf(3f, 2f)
)



val testEntries4 = listOf<ChartEntry>(
    entryOf(0f, 3f),
    entryOf(0f, 4f),
    entryOf(0f, 5f),
    entryOf(0f, 1f),
    entryOf(1f, 9f,),
    entryOf(2f, 4f),
    entryOf(3f, 11f),
    entryOf(3f, 11f)
)
@Composable
internal fun rememberChartStyle(chartColors: List<Color>) {
    rememberChartStyle(columnChartColors = chartColors, lineChartColors = chartColors)
}
@Composable
fun otherChart(){
    val chartEntryModelProducer1 = ChartEntryModelProducer(listOf(testEntries4, entriesList3))
//        listOf(entriesList1, entriesList2, entriesList3))
    ProvideChartStyle(LocalChartStyle.current) {
        val defaultColumns = currentChartStyle.columnChart.columns
        Chart(
            chart = columnChart(
                columns = remember(defaultColumns){
                    defaultColumns.map { defaultColumn ->
                        LineComponent(
                            defaultColumn.color,
                            defaultColumn.thicknessDp,
                            Shapes.cutCornerShape(topLeftPercent = COLUMN_CORNER_CUT_SIZE_PERCENT),
                        )
                    }
                },
                mergeMode = ColumnChart.MergeMode.Grouped
            ),
            chartModelProducer = chartEntryModelProducer1,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(valueFormatter = bottomAxisValueFormatter)
        )
    }

}

@Composable
fun BigBadChart(
    modelProducer: ChartEntryModelProducer,
    valueFormatter: AxisValueFormatter<AxisPosition.Horizontal.Bottom>
){

    ProvideChartStyle(rememberChartStyle(columnChartColors = chartColors, lineChartColors = chartColors)) {
        val defaultColumns = currentChartStyle.columnChart.columns
        Chart(
            chart = columnChart(
                columns = remember(defaultColumns){
                    defaultColumns.map { defaultColumn ->
                        LineComponent(
                            defaultColumn.color,
                            defaultColumn.thicknessDp,
                            Shapes.cutCornerShape(0)
                        )

                    }
                },
                mergeMode = ColumnChart.MergeMode.Grouped
            ),
            chartModelProducer = modelProducer,
            startAxis = rememberStartAxis(itemPlacer = startAxisItemPlacer),
            bottomAxis = rememberBottomAxis(valueFormatter = valueFormatter),
            legend = rememberLegend(),
        )
    }
}



@Composable
private fun rememberLegend(): HorizontalLegend {
    return HorizontalLegend(
        items = listOf(
            legendItem(
                icon = shapeComponent(Shapes.pillShape, Color(0xFF00FF00)),
                label = textComponent(
                    color = currentChartStyle.axis.axisLabelColor,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE,
                ),
                labelText = stringResource(R.string.taken)
            ),
            legendItem(
                icon = shapeComponent(Shapes.pillShape, Color(0xFFFF0000)),
                label = textComponent(
                    color = currentChartStyle.axis.axisLabelColor,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE,
                ),
                labelText = stringResource(R.string.missed)
            ),
            legendItem(
                icon = shapeComponent(Shapes.pillShape, Color(0xFFFFFF00)),
                label = textComponent(
                    color = currentChartStyle.axis.axisLabelColor,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE,
                ),
                labelText = stringResource(R.string.skipped)
            ),
            legendItem(
                icon = shapeComponent(Shapes.pillShape, Color(0xFFFFA500)),
                label = textComponent(
                    color = currentChartStyle.axis.axisLabelColor,
                    textSize = 12.sp,
                    typeface = Typeface.MONOSPACE,
                ),
                labelText = stringResource(R.string.rescheduled)
            )
        ),
        iconSizeDp = 8f,
        iconPaddingDp = 12f,
        spacingDp = 4f,
        padding = dimensionsOf(vertical = 8.dp)
    )
}


@Composable
fun TestLineChart(
    modelProducer: ChartEntryModelProducer
){
    val marker = rememberMarker()
    ProvideChartStyle(rememberChartStyle(lineChartColors = listOf(Color.Red), columnChartColors = listOf(
        Color.Blue))) {
        val defaultLines = currentChartStyle.lineChart.lines
        Chart(
            chart = lineChart(
                remember(defaultLines) {
                    defaultLines.map { defaultLine -> defaultLine.copy(lineBackgroundShader = null) }
                },
            ),
            chartModelProducer = modelProducer,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
            marker = rememberMarker(),
            legend = rememberLegend(),
            runInitialAnimation = false,
        )
    }
}


private const val PERSISTENT_MARKER_X = 10f


private const val MAX_START_AXIS_ITEM_COUNT = 6
private val startAxisItemPlacer = AxisItemPlacer.Vertical.default(MAX_START_AXIS_ITEM_COUNT)

private const val COLOR_1_CODE = 0xFF00FF00
private const val COLOR_2_CODE = 0xFFFF0000
private const val COLOR_3_CODE = 0xFFFFFF00
private const val COLOR_4_CODE = 0xFFFFA500
private const val COLUMN_CORNER_CUT_SIZE_PERCENT = 50

private val color1 = Color(COLOR_1_CODE)
private val color2 = Color(COLOR_2_CODE)
private val color3 = Color(COLOR_3_CODE)
private val color4 = Color(COLOR_4_CODE)
private val chartColors = listOf(color1, color2, color3, color4)

@Composable
internal fun rememberChartStyle(columnChartColors: List<Color>, lineChartColors: List<Color>): ChartStyle {
    val isSystemInDarkTheme = isSystemInDarkTheme()
    return remember(columnChartColors, lineChartColors, isSystemInDarkTheme) {
        val defaultColors = if (isSystemInDarkTheme) DefaultColors.Dark else DefaultColors.Light
        ChartStyle(
            ChartStyle.Axis(
                axisLabelColor = Color(defaultColors.axisLabelColor),
                axisGuidelineColor = Color(defaultColors.axisGuidelineColor),
                axisLineColor = Color(defaultColors.axisLineColor),
            ),
            ChartStyle.ColumnChart(
                columnChartColors.map { columnChartColor ->
                    LineComponent(
                        columnChartColor.toArgb(),
                        DefaultDimens.COLUMN_WIDTH,
                        Shapes.roundedCornerShape(DefaultDimens.COLUMN_ROUNDNESS_PERCENT),
                    )
                },
            ),
            ChartStyle.LineChart(
                lineChartColors.map { lineChartColor ->
                    LineChart.LineSpec(
                        lineColor = lineChartColor.toArgb(),
                        lineBackgroundShader = DynamicShaders.fromBrush(
                            Brush.verticalGradient(
                                listOf(
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                                    lineChartColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END),
                                ),
                            ),
                        ),
                    )
                },
            ),
            ChartStyle.Marker(),
            Color(defaultColors.elevationOverlayColor),
        )
    }
}

@Composable
internal fun rememberMarker(): Marker {
    val labelBackgroundColor = MaterialTheme.colorScheme.surface
    val labelBackground = remember(labelBackgroundColor) {
        ShapeComponent(labelBackgroundShape, labelBackgroundColor.toArgb()).setShadow(
            radius = LABEL_BACKGROUND_SHADOW_RADIUS,
            dy = LABEL_BACKGROUND_SHADOW_DY,
            applyElevationOverlay = true,
        )
    }
    val label = textComponent(
        background = labelBackground,
        lineCount = LABEL_LINE_COUNT,
        padding = labelPadding,
        typeface = Typeface.MONOSPACE,
    )
    val indicatorInnerComponent = shapeComponent(Shapes.pillShape, MaterialTheme.colorScheme.surface)
    val indicatorCenterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicatorOuterComponent = shapeComponent(Shapes.pillShape, Color.White)
    val indicator = overlayingComponent(
        outer = indicatorOuterComponent,
        inner = overlayingComponent(
            outer = indicatorCenterComponent,
            inner = indicatorInnerComponent,
            innerPaddingAll = indicatorInnerAndCenterComponentPaddingValue,
        ),
        innerPaddingAll = indicatorCenterAndOuterComponentPaddingValue,
    )
    val guideline = lineComponent(
        MaterialTheme.colorScheme.onSurface.copy(GUIDELINE_ALPHA),
        guidelineThickness,
        guidelineShape,
    )
    return remember(label, indicator, guideline) {
        object : MarkerComponent(label, indicator, guideline) {
            init {
                indicatorSizeDp = INDICATOR_SIZE_DP
                onApplyEntryColor = { entryColor ->
                    indicatorOuterComponent.color = entryColor.copyColor(INDICATOR_OUTER_COMPONENT_ALPHA)
                    with(indicatorCenterComponent) {
                        color = entryColor
                        setShadow(radius = INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS, color = entryColor)
                    }
                }
            }

            override fun getInsets(
                context: MeasureContext,
                outInsets: Insets,
                horizontalDimensions: HorizontalDimensions,
            ) = with(context) {
                outInsets.top = label.getHeight(context) + labelBackgroundShape.tickSizeDp.pixels +
                        LABEL_BACKGROUND_SHADOW_RADIUS.pixels * SHADOW_RADIUS_MULTIPLIER -
                        LABEL_BACKGROUND_SHADOW_DY.pixels
            }
        }
    }
}

private const val LABEL_BACKGROUND_SHADOW_RADIUS = 4f
private const val LABEL_BACKGROUND_SHADOW_DY = 2f
private const val LABEL_LINE_COUNT = 1
private const val GUIDELINE_ALPHA = .2f
private const val INDICATOR_SIZE_DP = 36f
private const val INDICATOR_OUTER_COMPONENT_ALPHA = 32
private const val INDICATOR_CENTER_COMPONENT_SHADOW_RADIUS = 12f
private const val GUIDELINE_DASH_LENGTH_DP = 8f
private const val GUIDELINE_GAP_LENGTH_DP = 4f
private const val SHADOW_RADIUS_MULTIPLIER = 1.3f

private val labelBackgroundShape = MarkerCorneredShape(Corner.FullyRounded)
private val labelHorizontalPaddingValue = 8.dp
private val labelVerticalPaddingValue = 4.dp
private val labelPadding = dimensionsOf(labelHorizontalPaddingValue, labelVerticalPaddingValue)
private val indicatorInnerAndCenterComponentPaddingValue = 5.dp
private val indicatorCenterAndOuterComponentPaddingValue = 10.dp
private val guidelineThickness = 2.dp
private val guidelineShape = DashedShape(Shapes.pillShape, GUIDELINE_DASH_LENGTH_DP, GUIDELINE_GAP_LENGTH_DP)



@Composable
fun TestNewChart(
modelProducer: ChartEntryModelProducer
){

}




@Preview(showBackground = true)
@Composable
fun PreviewCharts(){
    val chartEntryModelProducer1 = ChartEntryModelProducer(entriesOf(4f, 12f, 8f, 16f))
    TestLineChart(modelProducer = chartEntryModelProducer1)
//    TestBarChat()
//    otherChart()
}