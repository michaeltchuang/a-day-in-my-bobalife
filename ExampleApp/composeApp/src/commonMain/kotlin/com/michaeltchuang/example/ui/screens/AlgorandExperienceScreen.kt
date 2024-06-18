package com.michaeltchuang.example.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaeltchuang.example.ui.navigation.AlgorandExperienceNavigation
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.algorand_experience
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun AlgorandExperienceScreen() {
    val algorandBaseViewModel: AlgorandBaseViewModel =
        koinViewModel(
            viewModelStoreOwner = LocalContext.current as ComponentActivity,
        )

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState, modifier = Modifier)
        },
    ) { innerPadding ->

        val account by algorandBaseViewModel.accountStateFlow.collectAsStateWithLifecycle()

        if (account == null) {
            LoginScreen(innerPadding)
        } else {
            AlgorandExperienceNavigation(innerPadding)
        }

        val snackBarStateFlow by algorandBaseViewModel.snackBarStateFlow.collectAsStateWithLifecycle()
        if (snackBarStateFlow.trim().length > 0) {
            // val context = LocalContext.current
            LaunchedEffect(Unit) {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        duration = SnackbarDuration.Short,
                        message = snackBarStateFlow,
                    )
                    // Toast.makeText(context, snackBarStateFlow, Toast.LENGTH_SHORT).show()
                    launch {
                        delay(1000L)
                        algorandBaseViewModel.setSnackBarMessage("")
                    }
                }
            }
        }
    }
}
