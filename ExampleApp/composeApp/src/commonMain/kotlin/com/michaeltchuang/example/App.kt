package com.michaeltchuang.example

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.michaeltchuang.example.ui.navigation.AppNavigation
import com.michaeltchuang.example.ui.theme.AppTheme
import org.koin.compose.KoinContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun App() =
    AppTheme {
        KoinContext {
            MaterialTheme {
                AppNavigation()
            }
        }
    }

internal expect fun openUrl(url: String?)
