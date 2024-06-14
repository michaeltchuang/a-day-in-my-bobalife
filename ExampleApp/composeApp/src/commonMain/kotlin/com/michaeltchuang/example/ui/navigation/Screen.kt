package com.michaeltchuang.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home_screen")

    object CoinFlipper : Screen("coinflipper_screen")

    object ValidatorsList : Screen("validators_list_screen")

    object Play : Screen("play_screen")

    object Account : Screen("account_screen")
}
