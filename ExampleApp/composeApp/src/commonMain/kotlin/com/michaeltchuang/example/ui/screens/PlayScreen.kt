package com.michaeltchuang.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.michaeltchuang.example.theme.AppTheme
import example_app.composeapp.generated.resources.IndieFlower_Regular
import example_app.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun PlayScreen(navController: NavController) {
    AppTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "CoinFlipper",
                fontFamily = FontFamily(Font(Res.font.IndieFlower_Regular)),
                style = MaterialTheme.typography.displayLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}
