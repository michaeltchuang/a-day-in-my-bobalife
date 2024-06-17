package com.michaeltchuang.example.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaeltchuang.example.ui.navigation.AlgorandExperienceNavigation
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.algorand_experience
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlgorandExperienceScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = md_theme_light_primary,
                        titleContentColor = Color.White,
                    ),
                title = {
                    Text(
                        stringResource(resource = Res.string.algorand_experience),
                        maxLines = 1,
                    )
                },
            )
        },
    ) { innerPadding ->
        val activityViewModel: BaseViewModel = getKoin().get()
        val account by activityViewModel.accountStateFlow.collectAsStateWithLifecycle()
        if (account == null) {
            LoginScreen(innerPadding, activityViewModel)
        } else {
            AlgorandExperienceNavigation(innerPadding, activityViewModel)
        }
    }
}
