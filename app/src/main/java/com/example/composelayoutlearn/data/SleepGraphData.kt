package com.example.composelayoutlearn.data

import androidx.compose.ui.graphics.Color
import com.example.composelayoutlearn.R
import com.example.composelayoutlearn.ui.theme.Yellow_Awake
import com.example.composelayoutlearn.ui.theme.Yellow_Deep
import com.example.composelayoutlearn.ui.theme.Yellow_Light
import com.example.composelayoutlearn.ui.theme.Yellow_Rem
import java.time.Duration
import java.time.LocalDateTime

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn.data
作者: bobo
发布日期及时间: 2025/2/22 星期六  18:04
*/
data class SleepGraphData(
    val sleepDayData: List<SleepDayData>,
) {
    val earliestStartHour: Int by lazy {
        sleepDayData.minOf { it.firstSleepStart.hour }
    }
    val latestEndHour: Int by lazy {
        sleepDayData.maxOf { it.lastSleepEnd.hour }
    }
}

data class SleepDayData(
    val startDate: LocalDateTime,
    val sleepPeriod: List<SleepPeriod>,
    val sleepScore: Int,
) {
    val firstSleepStart: LocalDateTime by lazy {
        sleepPeriod.sortedBy(SleepPeriod::startTime).first().startTime
    }
    val lastSleepEnd: LocalDateTime by lazy {
        sleepPeriod.sortedBy(SleepPeriod::endTime).last().endTime
    }
    val totalTimeInBed: Duration by lazy {
        Duration.between(firstSleepStart, lastSleepEnd)
    }
    val sleepScoreEmoji: String by lazy {
        when (sleepScore) {
            in 0..40 -> "😖"
            in 41..60 -> "😏"
            in 60..70 -> "😴"
            in 71..100 -> "😃"
            else -> "🤷‍"
        }
    }

    fun fractionOfTotalTime(sleepPeriod: SleepPeriod): Float {
        return sleepPeriod.duration.toMinutes() / totalTimeInBed.toMinutes().toFloat()
    }

    fun minutesAfterSleepStart(sleepPeriod: SleepPeriod): Long {
        return Duration.between(
            firstSleepStart,
            sleepPeriod.startTime
        ).toMinutes()
    }

}

data class SleepPeriod(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val type: SleepType,
) {
    val duration: Duration by lazy {
        Duration.between(startTime, endTime)
    }
}

enum class SleepType(val title: Int, val color: Color) {
    Awake(R.string.sleep_type_awake, Yellow_Awake),
    REM(R.string.sleep_type_rem, Yellow_Rem),
    Light(R.string.sleep_type_light, Yellow_Light),
    Deep(R.string.sleep_type_deep, Yellow_Deep),
}
