package com.michaeltchuang.example.ui.screens

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.viewmodels.AlgorandBaseViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.ui.widgets.AlgorandDivider
import com.michaeltchuang.example.utils.Constants
import com.michaeltchuang.example.utils.getJsonDataFromAsset
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.coin_heads
import example_app.composeapp.generated.resources.coin_tails
import example_app.composeapp.generated.resources.play_balance
import example_app.composeapp.generated.resources.play_button_close_out
import example_app.composeapp.generated.resources.play_button_opt_in
import example_app.composeapp.generated.resources.play_button_pending
import example_app.composeapp.generated.resources.play_button_settle
import example_app.composeapp.generated.resources.play_button_settle_wait
import example_app.composeapp.generated.resources.play_button_submit_bet
import example_app.composeapp.generated.resources.play_button_wait_message
import example_app.composeapp.generated.resources.play_current_round
import example_app.composeapp.generated.resources.play_game_header
import example_app.composeapp.generated.resources.play_game_instructions
import example_app.composeapp.generated.resources.play_title
import example_app.composeapp.generated.resources.play_waiting_round
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun PlayCoinFlipperScreen() {
    val TAG = "PlayCoinFlipperScreen"

    val algorandBaseViewModel: AlgorandBaseViewModel =
        koinViewModel(
            viewModelStoreOwner = LocalContext.current as ComponentActivity,
        )
    if (algorandBaseViewModel.account == null) {
        Log.d(TAG, "No account detected")
        null.also { algorandBaseViewModel._account.value = it }
    }

    val appOptInStateFlow by algorandBaseViewModel.appOptInStateFlow.collectAsStateWithLifecycle()

    val playCoinFlipperViewModel: PlayCoinFlipperViewModel = getKoin().get()
    playCoinFlipperViewModel.algorandBaseViewModel = algorandBaseViewModel
    playCoinFlipperViewModel.abiContract = getJsonDataFromAsset(
        LocalContext.current, "CoinFlipper.arc4.json",
    ) ?: ""
    val snackbarMessageStateFlow by playCoinFlipperViewModel.snackBarMessageStateFlow.collectAsStateWithLifecycle()
    algorandBaseViewModel.setSnackBarMessage(snackbarMessageStateFlow)

    val mainHandler = Handler(Looper.getMainLooper())
    var refresh = Runnable {}
    refresh =
        Runnable {
            if (algorandBaseViewModel.hasExistingBet) {
                playCoinFlipperViewModel.hasExistingBetState()
            }
            mainHandler.postDelayed(refresh, 1000)
        }
    mainHandler.postDelayed(refresh, 1000)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        when (appOptInStateFlow) {
            true -> {
                // Allowed to Play
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .height(400.dp)
                                .fillMaxWidth(),
                    ) {
                        CoinFlipGameComposable(algorandBaseViewModel, playCoinFlipperViewModel)
                    }
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly,
                            modifier =
                                Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(),
                        ) {
                            AlgorandDivider()
                            AlgorandButton(
                                stringResourceId = Res.string.play_button_close_out,
                                onClick = {
                                    algorandBaseViewModel.account?.let {
                                        algorandBaseViewModel.closeOutApp(
                                            it,
                                            Constants.COINFLIP_APP_ID_TESTNET,
                                        )
                                    }
                                    algorandBaseViewModel.setSnackBarMessage(Res.string.play_button_wait_message)
                                },
                            )
                        }
                    }
                }
            }
            false -> {
                // Not allowed to Play
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                ) {
                    AlgorandButton(
                        stringResourceId = Res.string.play_button_opt_in,
                        onClick = {
                            algorandBaseViewModel.account?.let {
                                algorandBaseViewModel.appOptIn(
                                    it,
                                    Constants.COINFLIP_APP_ID_TESTNET,
                                )
                            }
                            algorandBaseViewModel.setSnackBarMessage(Res.string.play_button_wait_message)
                        },
                    )
                }
            }
            else -> {}
        }
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun CoinFlipGameComposable(algorandBaseViewModel: AlgorandBaseViewModel, playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    val appGameStateFlow: PlayCoinFlipperViewModel.GameState by playCoinFlipperViewModel.appGameStateFlow.collectAsStateWithLifecycle()

    when (appGameStateFlow) {
        PlayCoinFlipperViewModel.GameState.BET -> {
            // Bet Form
            BetViewComposable(algorandBaseViewModel, playCoinFlipperViewModel)
        }
        PlayCoinFlipperViewModel.GameState.PENDING -> {
            // Coin Flipping
            PendingViewComposable(playCoinFlipperViewModel)
        }
        PlayCoinFlipperViewModel.GameState.SETTLE -> {
            // Settle bet
            SettleViewComposable(algorandBaseViewModel, playCoinFlipperViewModel)
        }
        PlayCoinFlipperViewModel.GameState.OTHER -> {
            // Default First View
            if (playCoinFlipperViewModel.algorandBaseViewModel?.hasExistingBet!!) {
                playCoinFlipperViewModel.hasExistingBetState()
            } else {
                // Bet Form
                BetViewComposable(algorandBaseViewModel, playCoinFlipperViewModel)
            }
        }
    }
}

@Composable
fun BetViewComposable(algorandBaseViewModel: AlgorandBaseViewModel, playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
    ) {
        Text(
            stringResource(Res.string.play_title),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style =
                TextStyle(
                    fontSize = 24.sp,
                ),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
        )
        playCoinFlipperViewModel.algorandBaseViewModel?.accountInfo?.let {
            Text(
                stringResource(Res.string.play_balance) + " ${it.amount} mAlgos",
                color = Color.Black,
                style =
                    TextStyle(
                        fontSize = 16.sp,
                    ),
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
            )
        }
        AlgorandDivider()
        Text(
            stringResource(Res.string.play_game_header),
            color = Color.Black,
            style =
                TextStyle(
                    fontSize = 16.sp,
                ),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
        )
        Row(
            modifier =
                Modifier
                    .heightIn(max = 100.dp)
                    .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.coin_heads),
                contentDescription =
                    stringResource(
                        Res.string.play_game_instructions,
                    ),
                modifier =
                    Modifier
                        .fillMaxHeight()
//                    .weight(
//                        weight = painter.intrinsicSize.width / painter.intrinsicSize.height,
//                        fill = false
//                    )
                        .clickable {
                            playCoinFlipperViewModel.isHeads = false
                            playCoinFlipperViewModel.updateGameState()
                            algorandBaseViewModel.setSnackBarMessage(Res.string.play_button_submit_bet)
                            Modifier.alpha(1.0f)
                        },
            )
            Image(
                painter = painterResource(Res.drawable.coin_tails),
                contentDescription =
                    stringResource(
                        Res.string.play_game_instructions,
                    ),
                modifier =
                    Modifier
                        .fillMaxHeight()
//                    .weight(
//                        weight = painter.intrinsicSize.width / painter.intrinsicSize.height,
//                        fill = false
//                    )
                        .clickable {
                            playCoinFlipperViewModel.isHeads = false
                            playCoinFlipperViewModel.updateGameState()
                            algorandBaseViewModel.setSnackBarMessage(Res.string.play_button_submit_bet)
                            Modifier.alpha(1.0f)
                        },
            )
        }
        AlgorandDivider()
        Text(
            text = stringResource(Res.string.play_game_instructions),
            color = Color.Black,
            style =
                TextStyle(
                    fontSize = 16.sp,
                ),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(start = 30.dp, end = 30.dp),
        )
    }
}

@Composable
fun PendingViewComposable(playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
    ) {
        ProgressComposable(playCoinFlipperViewModel)
        AlgorandButton(
            stringResourceId = Res.string.play_button_pending,
            onClick = { },
        )
    }
}

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SettleViewComposable(algorandBaseViewModel: AlgorandBaseViewModel, playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier =
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
    ) {
        ProgressComposable(playCoinFlipperViewModel)
        AlgorandButton(
            stringResourceId = Res.string.play_button_settle,
            onClick = {
                playCoinFlipperViewModel.updateGameState()
                algorandBaseViewModel.setSnackBarMessage(Res.string.play_button_settle_wait)
            },
        )
    }
}


@Composable
fun ProgressComposable(playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    val currentRound by playCoinFlipperViewModel.currentRoundStateFlow.collectAsStateWithLifecycle()

    if (currentRound != 0L) {
        Text(
            stringResource(Res.string.play_current_round) + " ${currentRound}",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style =
                TextStyle(
                    fontSize = 24.sp,
                ),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
        )
        Text(
            stringResource(Res.string.play_waiting_round) + " ${(playCoinFlipperViewModel.commitmentRound + 10L)}",
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            style =
                TextStyle(
                    fontSize = 24.sp,
                ),
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
        )
        CircularProgressIndicator(
            progress = { playCoinFlipperViewModel.calculateProgress() },
            modifier = Modifier.size(size = 100.dp),
            color = md_theme_light_primary,
            strokeWidth = 8.dp,
            trackColor = Color.Gray,
            strokeCap = StrokeCap.Round,
        )
        AlgorandDivider()
    }
}


