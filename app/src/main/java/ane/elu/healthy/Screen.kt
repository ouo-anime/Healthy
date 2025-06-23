package ane.elu.healthy

sealed class Screen(val route: String) {
    data object home: Screen("home")
    data object notes: Screen("notes")
    data object clock: Screen("clock")
    data object calc: Screen("calc")
}