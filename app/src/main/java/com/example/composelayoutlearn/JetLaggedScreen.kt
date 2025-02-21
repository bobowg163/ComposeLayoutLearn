package com.example.composelayoutlearn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2025/2/21 星期五  23:23
*/

@Preview(showBackground = true)
@Preview(device = "spec:width=670dp,height=800dp", showBackground = true)
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
        Column (
            modifier = Modifier.yellowBackground()
        ){
//            JetLaggedHeader(
//            )
        }
    }
}