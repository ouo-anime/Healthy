package ane.elu.healthy

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.rounded.Fastfood
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by derivedStateOf { currentBackStackEntry?.destination?.route }

    val buttons = listOf(
        ButtonData(
            text = "Home",
            icon = Icons.Outlined.Hub,
            route = Screen.home.route
        ),
        ButtonData(
            text = "Calc",
            icon = Icons.Rounded.Fastfood,
            route = Screen.calc.route
        )
    )

    Scaffold(
        bottomBar = {
            AnimatedNavigationBar(
                buttons = buttons,
                selectedIndex = buttons.indexOfFirst {
                    it.route == (currentRoute ?: Screen.home.route)
                }.coerceAtLeast(0),
                onItemClick = { index ->
                    navController.navigate(buttons[index].route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.home.route) { HomeScreen() }
            composable(Screen.calc.route) { CalcScreen() }
        }
    }
}