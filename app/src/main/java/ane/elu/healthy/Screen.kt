package ane.elu.healthy

sealed class Screen(val route: String) {
    data object home: Screen("home")
    data object calc: Screen("calc")
}