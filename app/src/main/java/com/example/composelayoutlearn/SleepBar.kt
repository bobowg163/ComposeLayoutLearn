package com.example.composelayoutlearn

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.example.composelayoutlearn.data.SleepDayData
import com.example.composelayoutlearn.data.SleepPeriod
import com.example.composelayoutlearn.data.SleepType
import com.example.composelayoutlearn.ui.theme.LegendHeadingStyle

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2025/2/22 星期六  19:50
*/
private val lineThickness = 2.dp
private val barHeight = 24.dp
private const val animationDuration = 500
private val textPadding = 4.dp
private val sleepGradientBarColorStops: List<Pair<Float, Color>> = SleepType.entries.map {
    Pair(
        when (it) {
            SleepType.Awake -> 0f
            SleepType.REM -> 0.33f
            SleepType.Light -> 0.66f
            SleepType.Deep -> 1f
        },
        it.color
    )
}


@Composable
fun SleepBar(
    modifier: Modifier = Modifier,
    sleepdata: SleepDayData,
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val transition = updateTransition(targetState = isExpanded, label = "expanded")
    Column(
        modifier = modifier.clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }
        ) { isExpanded = !isExpanded },
    ) {
        SleepRoundedBar(
            sleepdata,
            transition,
        )
    }
}

@Composable
private fun SleepRoundedBar(sleepdata: SleepDayData, transition: Transition<Boolean>) {
    val textMeasurer = rememberTextMeasurer()
    val height by transition.animateDp(label = "height", transitionSpec = {
        spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    }) { expanded ->
        if (expanded) 100.dp else 24.dp
    }
    val animationProgress by transition.animateFloat(label = "progress", transitionSpec = {
        spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
    }) { target ->
        if (target) 1f else 0f
    }
    Spacer(
        modifier = Modifier
            .drawWithCache {
                val width = this.size.width
                val cornerRadiusStartPx = 2.dp.toPx()
                val collapsedCornerRadiusPx = 10.dp.toPx()
                val animatedCornerRadius = CornerRadius(
                    lerp(
                        cornerRadiusStartPx,
                        collapsedCornerRadiusPx,
                        (1 - animationProgress)
                    )
                )
                val lineThicknessPx = lineThickness.toPx()
                val roundedRectPath = Path()
                roundedRectPath.addRoundRect(
                    RoundRect(
                        rect = Rect(
                            Offset(x = 0f, y = -lineThicknessPx / 2f),
                            Size(
                                this.size.width + lineThicknessPx * 2,
                                this.size.height + lineThicknessPx
                            )
                        ),
                        cornerRadius = animatedCornerRadius
                    )
                )
                val roundedCornerStroke = Stroke(
                    lineThicknessPx,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.cornerPathEffect(cornerRadiusStartPx * animationProgress)
                )
                val barHeightPx = barHeight.toPx()
                val sleepGraphPath = generateSleepPath(
                    this.size,
                    sleepdata,
                    width,
                    barHeightPx,
                    animationProgress,
                    lineThickness.toPx() / 2f
                )
                val gradientBrush = Brush.verticalGradient(
                    colorStops = sleepGradientBarColorStops.toTypedArray(),
                    startY = 0f,
                    endY = SleepType.entries.size * barHeightPx
                )
                val textResult = textMeasurer.measure(AnnotatedString(sleepdata.sleepScoreEmoji))

                onDrawBehind {
                    drawSleepBar(
                        roundedRectPath,
                        sleepGraphPath,
                        gradientBrush,
                        roundedCornerStroke,
                        animationProgress,
                        textResult,
                        cornerRadiusStartPx
                    )
                }
            }
            .height(height)
            .fillMaxWidth()

    )
}

private fun generateSleepPath(
    canvasSize: Size,
    sleepData: SleepDayData,
    width: Float,
    barHeightPx: Float,
    heightAnimation: Float,
    lineThicknessPx: Float
): Path {
    val path = Path()
    var previousPeriod: SleepPeriod? = null
    path.moveTo(0f, 0f)
    sleepData.sleepPeriod.forEach { period ->
        val percentageOfTotal = sleepData.fractionOfTotalTime(period)
        val periodWidth = percentageOfTotal * width
        val startOffsetPercentage = sleepData.minutesAfterSleepStart(period) /
                sleepData.totalTimeInBed.toMinutes().toFloat()
        val halfBarHeight = canvasSize.height / SleepType.entries.size / 2f

        val offset = if (previousPeriod == null) {
            0f
        } else {
            halfBarHeight
        }

        val offsetY = lerp(
            0f,
            period.type.heightSleepType() * canvasSize.height, heightAnimation
        )
        // step 1 - draw a line from previous sleep period to current
        if (previousPeriod != null) {
            path.lineTo(
                x = startOffsetPercentage * width + lineThicknessPx,
                y = offsetY + offset
            )
        }

        // step 2 - add the current sleep period as rectangle to path
        path.addRect(
            rect = Rect(
                offset = Offset(x = startOffsetPercentage * width + lineThicknessPx, y = offsetY),
                size = canvasSize.copy(width = periodWidth, height = barHeightPx)
            )
        )
        // step 3 - move to the middle of the current sleep period
        path.moveTo(
            x = startOffsetPercentage * width + periodWidth + lineThicknessPx,
            y = offsetY + halfBarHeight
        )

        previousPeriod = period
    }
    return path
}

@Composable
private fun LegendItem(sleepType: SleepType) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(sleepType.color)
        )
        Text(
            stringResource(id = sleepType.title),
            style = LegendHeadingStyle,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

private fun DrawScope.drawSleepBar(
    roundedRectPath:Path,
    sleepGraphPath:Path,
    gradientBrush:Brush,
    roundedCornerStroke:Stroke,
    animationProgress:Float,
    textResult:TextLayoutResult,
    cornerRadiusStartPx:Float
) {
    clipPath(roundedRectPath) {
        drawPath(sleepGraphPath, brush = gradientBrush)
        drawPath(sleepGraphPath, style = roundedCornerStroke, brush = gradientBrush)
    }
    translate(left = -animationProgress*(textResult.size.width+ textPadding.toPx())){
        drawText(textResult, topLeft = Offset(textPadding.toPx(),cornerRadiusStartPx))
    }
}


@Preview
@Composable
private fun DetailLegend() {
    Row(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SleepType.values().forEach {
            LegendItem(it)
        }
    }
}


private fun SleepType.heightSleepType(): Float {
    return when (this) {
        SleepType.Awake -> 0f
        SleepType.REM -> 0.25f
        SleepType.Light -> 0.5f
        SleepType.Deep -> 0.75f
    }
}