package com.example.composelayoutlearn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.composelayoutlearn.ui.theme.SmallHeadingStyle
import com.example.composelayoutlearn.ui.theme.White
import com.example.composelayoutlearn.ui.theme.Yellow

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2025/2/22 星期六  17:36
*/
@Composable
fun JetLaggedTabs(
    modifier: Modifier = Modifier,
    selectedTab: SleepTab,
    onTabSelected: (SleepTab) -> Unit
) {
    ScrollableTabRow(
        modifier = modifier,
        edgePadding = 12.dp,
        selectedTabIndex = selectedTab.ordinal,
        containerColor = White,
        indicator = { tabPositions: List<TabPosition> ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab.ordinal])
                    .fillMaxSize()
                    .padding(horizontal = 2.dp)
                    .border(
                        BorderStroke(2.dp, Yellow), RoundedCornerShape(10.dp)
                    )
            )
        },
        divider = {}
    ) {
        SleepTab.entries.forEachIndexed { index, sleepTab ->
            val selected = index == selectedTab.ordinal
            SleepTabText(
                sleepTab = sleepTab,
                selected = selected,
                index = index,
                onTabSelected = onTabSelected
            )
        }
    }
}

private val textModifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp)

@Composable
private fun SleepTabText(
    sleepTab: SleepTab,
    selected: Boolean,
    index: Int,
    onTabSelected: (SleepTab) -> Unit
) {
    Tab(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(16.dp)),
        selected = selected,
        unselectedContentColor = Color.Black,
        onClick = {
            onTabSelected(SleepTab.entries[index])
        }
    ) {
        Text(
            text = stringResource(id=sleepTab.title),
            modifier = textModifier,
            style = SmallHeadingStyle
        )
    }

}