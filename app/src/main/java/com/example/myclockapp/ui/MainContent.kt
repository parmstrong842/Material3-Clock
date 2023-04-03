package com.example.myclockapp.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Watch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myclockapp.AppViewModelProvider
import com.example.myclockapp.IconResource
import com.example.myclockapp.R
import com.example.myclockapp.ui.theme.MyClockAppTheme


enum class Screen {
    NewAlarm,
}

@Composable
fun MainContent() {
    val mainViewModel: MainContentViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val mainUiState by mainViewModel.uiState.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        "${Screen.NewAlarm.name}/{alarmId}" -> mainViewModel.bottomBarState(false)
        else -> { mainViewModel.bottomBarState(true) }
    }

    Scaffold(
        bottomBar = {
            MyBottomBar(
                navController = navController,
                updateStartScreenState = { mainViewModel.updateStartScreenState(it) },
                bottomBarState = mainUiState.bottomBarState
            )
        }
    ) { innerPadding ->
        val startScreen = when(mainViewModel.startScreen) {
            0 -> BottomNavigationScreen.Alarm.route
            1 -> BottomNavigationScreen.WorldClock.route
            2 -> BottomNavigationScreen.Stopwatch.route
            3 -> BottomNavigationScreen.Timer.route
            else -> BottomNavigationScreen.Alarm.route
        }

        NavHost(navController, startScreen) {
            composable(BottomNavigationScreen.Alarm.route) {
                AlarmScreen(
                    onNewAlarmClick = {
                        navController.navigate("${Screen.NewAlarm.name}/-1")
                    },
                    editAlarmClick = {
                        navController.navigate("${Screen.NewAlarm.name}/$it")
                    },
                    innerPadding = innerPadding
                )
            }
            composable(
                route = "${Screen.NewAlarm.name}/{alarmId}",
                arguments = listOf(navArgument("alarmId") {
                    type = NavType.StringType
                })
            ) {
                NewAlarmScreen(
                    onCancelClicked = {
                        navController.popBackStack()
                    },
                    onSaveClicked = {
                        navController.popBackStack()
                    },
                )
            }
            composable(BottomNavigationScreen.WorldClock.route) {
                WorldClockScreen()
            }
            composable(BottomNavigationScreen.Stopwatch.route) {
                StopwatchScreen(innerPadding = innerPadding)
            }
            composable(BottomNavigationScreen.Timer.route) {
                TimerScreen()
            }
        }
    }
}

@Composable
private fun MyBottomBar(
    navController: NavController,
    updateStartScreenState: (Int) -> Unit,
    bottomBarState: Boolean,
) {
    val selectedStyle = MaterialTheme.typography.labelMedium.copy(
        color = Color.Black,
        fontWeight = FontWeight.Bold
    )

    val items = listOf(
        BottomNavigationScreen.Alarm,
        BottomNavigationScreen.WorldClock,
        BottomNavigationScreen.Stopwatch,
        BottomNavigationScreen.Timer
    )

    AnimatedVisibility(
        visible = bottomBarState,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            items.forEachIndexed { index, screen ->
                val selected = navBackStackEntry?.destination?.route == screen.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            screen.icon.asPainterResource(filled = selected),
                            contentDescription = screen.route
                        )
                    },
                    label = {
                        Text(
                            text = screen.route,
                            style = if (selected) selectedStyle else MaterialTheme.typography.labelMedium
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (navController.currentDestination?.route != screen.route) {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                            updateStartScreenState(index)
                        }
                    }
                )
            }
        }
    }
}

sealed class BottomNavigationScreen(val route: String, val icon: IconResource) {
    object Alarm : BottomNavigationScreen(
        "Alarm",
        IconResource.fromImageVector(Icons.Filled.Alarm, Icons.Outlined.Alarm)
    )
    object WorldClock : BottomNavigationScreen(
        "World Clock",
        IconResource.fromDrawableResource(R.drawable.outline_public_24)
    )
    object Stopwatch : BottomNavigationScreen(
        "Stopwatch",
        IconResource.fromImageVector(Icons.Filled.Watch, Icons.Outlined.Watch)
    )
    object Timer : BottomNavigationScreen(
        "Timer",
        IconResource.fromImageVector(Icons.Filled.Timer, Icons.Outlined.Timer)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyClockAppTheme {
        MainContent()
    }
}