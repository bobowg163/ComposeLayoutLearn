package com.example.composelayoutlearn

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composelayoutlearn.data.SleepGraphData

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2025/2/21 星期五  23:23
*/

@Preview(showBackground = true)
@Preview(device = "spec:width=673dp,height=841dp", showBackground = true)
@Composable
fun JetLaggedScreen(
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(Color.White)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.yellowBackground()
        ) {
            JetLaggedHeader(
                modifier = Modifier.fillMaxWidth(),
                onDrawerClicked = onDrawerClicked
            )
            Spacer(modifier = Modifier.height(32.dp))
            JetLaggedSleepSummary(modifier = Modifier.padding(start = 16.dp, end = 16.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))

        var selectedTab by remember { mutableStateOf(SleepTab.Week) }
        JetLaggedTabs(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        val sleepState by remember { mutableStateOf(sleepData) }
        JetLaggedSleepGraph(sleepState)
    }
}

@Composable
fun JetLaggedSleepGraph(
    sleepGraphData: SleepGraphData
) {
    val scrollState = rememberScrollState()
    val hours = (sleepGraphData.earliestStartHour..23) + (0..sleepGraphData.latestEndHour)
    TimeGraph(
        modifier = Modifier.horizontalScroll(scrollState).wrapContentSize(),
        dayItemsCount = sleepGraphData.sleepDayData.size,
        hoursHeader = {
            HoursHeader(
                hours = hours
            )
        },
        dayLabel = {},
        bar = {}
    )
}

@Composable
private fun HoursHeader(hours: List<Int>) {
    TODO("Not yet implemented")
}


enum class SleepTab(val title: Int) {
    Day(R.string.sleep_tab_day_heading),
    Week(R.string.sleep_tab_week_heading),
    Month(R.string.sleep_tab_month_heading),
    SixMonths(R.string.sleep_tab_six_months_heading),
    OneYear(R.string.sleep_tab_one_year_heading),
}