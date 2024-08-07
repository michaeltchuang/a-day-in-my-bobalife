package com.michaeltchuang.example.ui.screens

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.michaeltchuang.example.ui.navigation.Screen
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.utils.Constants
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@SuppressLint("ComposableNaming", "SetJavaScriptEnabled")
@Composable
fun DispenserScreen(
    navController: NavController,
    algorandBaseViewModel: AlgorandBaseViewModel,
    tag: String,
) {
    BackHandler {
        navController.popBackStack(Screen.HomeScreen.route, false)
    }

    algorandBaseViewModel.appOptInStateCheck(algorandBaseViewModel.account!!, Constants.COINFLIP_APP_ID_TESTNET)
    val url = "https://bank.testnet.algorand.network/?account=${algorandBaseViewModel.account?.address}"

    AndroidView(
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(true)
            }
        },
        update = { webView ->
            webView.setBackgroundColor(0x00000000)
            webView.loadUrl(url)
        }
    )
}


