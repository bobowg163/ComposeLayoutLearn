package com.example.composelayoutlearn

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2/18/25 Tuesday  7:03 PM
*/

@Composable
fun CustomLayout(
    Content: @Composable () -> Unit, modifier: Modifier = Modifier
) {
    Layout(content = {}, measurePolicy = {measure , constraints ->
        layout(500,500){

        }
    })
}