package com.example.composelayoutlearn

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/*
项目:ComposeLayoutLearn
包名：com.example.composelayoutlearn
作者: bobo
发布日期及时间: 2/18/25 Tuesday  7:03 PM
*/

@Composable
fun HomeScreenDrawer() {
    Surface(modifier = Modifier.fillMaxSize()) {
        var drawerState by remember { mutableStateOf(com.example.composelayoutlearn.DrawerState.Closed) }
        var screenState by remember { mutableStateOf(Screen.Home) }
        val translationX = remember { Animatable(0f) }
        val drawerWidth = with(LocalDensity.current) {
            DrawerWidth.toPx()
        }
        translationX.updateBounds(0f, drawerWidth)
        val coroutineScope = rememberCoroutineScope()

        fun toggleDrawerState() {
            coroutineScope.launch {
                if (drawerState == com.example.composelayoutlearn.DrawerState.Open) {
                    translationX.animateTo(0f)
                } else {
                    translationX.animateTo(drawerWidth)
                }
                drawerState = if (drawerState == com.example.composelayoutlearn.DrawerState.Open) {
                    com.example.composelayoutlearn.DrawerState.Closed
                } else {
                    com.example.composelayoutlearn.DrawerState.Open
                }
            }
        }

        HomeScreenDrawerContents(
            selectedScreen = screenState,
            onScreenSelected = { screen ->
                screenState = screen
            }
        )
    }
}

@Composable
private fun HomeScreenDrawerContents(
    selectedScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Screen.entries.forEach {
            NavigationDrawerItem(
                label = {
                    Text(it.text)
                },
                icon = {
                    Icon(imageVector = it.icon, contentDescription = it.text)
                },
                colors =
                NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.White),
                selected = selectedScreen == it,
                onClick = {
                    onScreenSelected(it)
                },
            )
        }
    }
}

@Composable
private fun ScreenContents(
    selectedScreen: Screen,
    onDrawerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box (modifier=modifier){
        when (selectedScreen) {
            Screen.Home ->  JetLaggedScreen(
                modifier = Modifier,
                onDrawerClicked = onDrawerClicked
            )
//            Screen.SleepDetails -> SleepDetailsScreen()
//            Screen.Leaderboard -> LeaderboardScreen()
//            Screen.Settings -> SettingsScreen()

            else -> {}
        }
    }
}

private enum class DrawerState {
    Open, Closed
}

private val DrawerWidth = 300.dp

private enum class Screen(val text: String, val icon: ImageVector) {
    Home("Home", Icons.Default.Home),
    SleepDetails("Sleep", Icons.Default.Bedtime),
    Leaderboard("Leaderboard", Icons.Default.Leaderboard),
    Settings("Settings", Icons.Default.Settings)
}