package com.michaeltchuang.example.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.michaeltchuang.example.ui.screens.AccountScreen
import com.michaeltchuang.example.ui.screens.PlayCoinFlipperScreen
import com.michaeltchuang.example.ui.screens.ValidatorsListScreen

@Composable
fun AlgorandExperienceNavigation(innerPadding: PaddingValues) {
    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Account", "Coin Flipper", "Validators")

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = innerPadding.calculateTopPadding()),
    ) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    icon = {
                        when (index) {
                            0 -> Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                            1 -> Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                            2 -> Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        }
                    },
                )
            }
        }
        when (tabIndex) {
            0 -> AccountScreen()
            1 -> PlayCoinFlipperScreen()
            2 -> ValidatorsListScreen()
        }
    }
}
