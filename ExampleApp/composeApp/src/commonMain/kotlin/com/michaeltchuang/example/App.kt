package com.michaeltchuang.example

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.michaeltchuang.example.ui.navigation.BottomNavigationBar
import com.michaeltchuang.example.ui.theme.AppTheme
import org.koin.compose.KoinContext

@Composable
internal fun App() =
    AppTheme {
        KoinContext {
            MaterialTheme {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BottomNavigationBar()
                }
            }
        }
    }

internal expect fun openUrl(url: String?)
