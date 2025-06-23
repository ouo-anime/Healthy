package ane.elu.healthy

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Notes
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

data class NavigationBarInfo(
    val buttons: List<ButtonData>,
    val selectedIndex: Int,
    val onItemClick: (Int) -> Unit
)

@SuppressLint("UnrememberedMutableState")
@Composable
fun MainContentAndNavLogic(
    navController: NavHostController,
    onProvideNavigationBarInfo: (NavigationBarInfo) -> Unit,
    onScrollChange: (Boolean) -> Unit
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by derivedStateOf { currentBackStackEntry?.destination?.route }

    val buttons = listOf(
        ButtonData(
            text = "Home",
            icon = Icons.Outlined.Hub,
            route = Screen.home.route
        ),
        ButtonData(
            text = "Notes",
            icon = Icons.AutoMirrored.Outlined.Notes,
            route = Screen.notes.route
        ),
        ButtonData(
            text = "Clock",
            icon = Icons.Outlined.Timer,
            route = Screen.clock.route
        ),
        ButtonData(
            text = "Diabetes",
            icon = Icons.Outlined.WaterDrop,
            route = Screen.calc.route
        )
    )

    val selectedIndex = buttons.indexOfFirst {
        it.route == (currentRoute ?: Screen.home.route)
    }.coerceAtLeast(0)

    val onItemClick: (Int) -> Unit = { index ->
        navController.navigate(buttons[index].route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    LaunchedEffect(buttons, selectedIndex, onItemClick) {
        onProvideNavigationBarInfo(NavigationBarInfo(buttons, selectedIndex, onItemClick))
    }

    NavHost(
        navController = navController,
        startDestination = Screen.home.route,
    ) {
        composable(Screen.home.route) {
            HomeScreen(onScrollChange = onScrollChange)
        }
        composable(Screen.notes.route) { NotesScreen() }
        composable(Screen.clock.route) { ClockScreen() }
        composable(Screen.calc.route) { DiabetesScreen() }
    }
}