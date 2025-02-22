package com.example.composelayoutlearn

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composelayoutlearn.ui.theme.HeadingStyle
import com.example.composelayoutlearn.ui.theme.SmallHeadingStyle
import com.example.composelayoutlearn.ui.theme.TitleBarStyle

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2025/2/22 星期六  14:22
*/

@Preview(showBackground = true)
@Composable
fun JetLaggedHeader(
    modifier: Modifier = Modifier,
    onDrawerClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier.height(150.dp)
    ) {
        Row(
            modifier = Modifier.windowInsetsPadding(insets = WindowInsets.systemBars)
        ) {
            IconButton(onClick = onDrawerClicked) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.not_implemented)
                )
            }
            Text(
                text = stringResource(R.string.jetlagged_app_heading),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                style = TitleBarStyle,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Preview
@Composable
fun JetLaggedSleepSummary(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(R.string.average_time_in_bed_heading),
                style = SmallHeadingStyle
            )
            Text(
                text = stringResource(R.string.placeholder_text_ave_time),
                style = HeadingStyle
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = stringResource(R.string.average_time_in_bed_heading),
                style = SmallHeadingStyle
            )
            Text(
                text = stringResource(R.string.placeholder_text_ave_time_2),
                style = HeadingStyle
            )
        }
    }
    Spacer(modifier = Modifier.height(32.dp))
}