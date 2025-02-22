package com.example.composelayoutlearn

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
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
        var drawerState by remember { mutableStateOf(DrawerState.Closed) }
        var screenState by remember { mutableStateOf(Screen.Home) }
        val translationX = remember { Animatable(0f) }
        val drawerWidth = with(LocalDensity.current) {
            DrawerWidth.toPx()
        }
        translationX.updateBounds(0f, drawerWidth)
        val coroutineScope = rememberCoroutineScope()

        fun toggleDrawerState() {
            coroutineScope.launch {
                if (drawerState == DrawerState.Open) {
                    translationX.animateTo(0f)
                } else {
                    translationX.animateTo(drawerWidth)
                }
                drawerState = if (drawerState == DrawerState.Open) {
                    DrawerState.Closed
                } else {
                    DrawerState.Open
                }
            }
        }

        HomeScreenDrawerContents(
            selectedScreen = screenState,
            onScreenSelected = { screen ->
                screenState = screen
            }
        )

        val draggableState = rememberDraggableState(
            onDelta = { dragAmount ->
                coroutineScope.launch {
                    translationX.snapTo(translationX.value + dragAmount)
                }
            }
        )
        val decay = rememberSplineBasedDecay<Float>()
        ScreenContents(
            selectedScreen = screenState,
            onDrawerClicked = ::toggleDrawerState,
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX.value
                    val scale = lerp(1f, 0.8f, translationX.value / drawerWidth)
                    this.scaleX = scale
                    this.scaleY = scale
                    val roundedCorners = lerp(0f, 32.dp.toPx(), translationX.value / drawerWidth)
                    this.shape = RoundedCornerShape(roundedCorners)
                    this.clip = true
                    this.shadowElevation = 32f
                }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        val targetOffsetX = decay.calculateTargetValue(translationX.value, velocity)
                        coroutineScope.launch {
                            val actualTargetx = if (targetOffsetX > drawerWidth * 0.5) {
                                drawerWidth
                            } else {
                                0f
                            }
                            val targetDifference = (actualTargetx - targetOffsetX)
                            val canReachTargetWithDecay =
                                (targetOffsetX > actualTargetx && velocity > 0f) || (targetOffsetX < actualTargetx && velocity < 0 && targetDifference < 0f)
                            if (canReachTargetWithDecay) {
                                translationX.animateDecay(
                                    initialVelocity = velocity,
                                    animationSpec = decay
                                )
                            } else {
                                translationX.animateTo(actualTargetx, initialVelocity = velocity)
                            }
                            drawerState = if (actualTargetx == drawerWidth) {
                                DrawerState.Open
                            } else {
                                DrawerState.Closed
                            }
                        }
                    }
                )
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
    Box(modifier = modifier) {
        when (selectedScreen) {
            Screen.Home -> JetLaggedScreen(
                modifier = Modifier,
                onDrawerClicked = onDrawerClicked
            )
            Screen.SleepDetails -> Surface (modifier=Modifier.fillMaxSize()){
            }
            Screen.Leaderboard ->Surface (modifier=Modifier.fillMaxSize()){}
            Screen.Settings -> Surface (modifier=Modifier.fillMaxSize()){}

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