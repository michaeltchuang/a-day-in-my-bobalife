package com.michaeltchuang.example.ui.screens

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.algorand.example.coinflipper.utils.Constants
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.viewmodels.BaseViewModel
import com.michaeltchuang.example.ui.viewmodels.PlayCoinFlipperViewModel
import com.michaeltchuang.example.ui.widgets.AlgorandButton
import com.michaeltchuang.example.ui.widgets.AlgorandDivider
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.coin_heads
import example_app.composeapp.generated.resources.coin_tails
import example_app.composeapp.generated.resources.play_balance
import example_app.composeapp.generated.resources.play_button_close_out
import example_app.composeapp.generated.resources.play_button_opt_in
import example_app.composeapp.generated.resources.play_button_pending
import example_app.composeapp.generated.resources.play_button_settle
import example_app.composeapp.generated.resources.play_current_round
import example_app.composeapp.generated.resources.play_game_header
import example_app.composeapp.generated.resources.play_game_instructions
import example_app.composeapp.generated.resources.play_title
import example_app.composeapp.generated.resources.play_waiting_round
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.getKoin

@Composable
fun PlayCoinFlipperScreen(activityViewModel: BaseViewModel) {
    PlayFragmentComposable(activityViewModel)
}

@Composable
fun PlayFragmentComposable(activityViewModel: BaseViewModel) {
    val playCoinFlipperViewModel: PlayCoinFlipperViewModel = getKoin().get()
    val appOptInStateFlow by playCoinFlipperViewModel.appOptInStateFlow.collectAsStateWithLifecycle()
    val snackBarStateFlow by playCoinFlipperViewModel.snackBarStateFlow.collectAsStateWithLifecycle()

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
                        CoinFlipGameComposable(playCoinFlipperViewModel)
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
                                    activityViewModel.accountInfo?.let {
                                        activityViewModel.closeOutApp(
                                            activityViewModel._account.value!!,
                                            Constants.COINFLIP_APP_ID_TESTNET,
                                        )
                                    }
                                },
                                // ShowSnackbar(message = stringResource(Res.string.play_button_wait_message)
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
                            activityViewModel.accountInfo?.let {
                                activityViewModel.appOptIn(
                                    activityViewModel._account.value!!,
                                    Constants.COINFLIP_APP_ID_TESTNET,
                                )
                            }
                            // ShowSnackbar(message = stringResource(Res.string.play_button_wait_message))
                        },
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
fun CoinFlipGameComposable(playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    val appGameStateFlow: PlayCoinFlipperViewModel.GameState by playCoinFlipperViewModel.appGameStateFlow.collectAsStateWithLifecycle()

    when (appGameStateFlow) {
        PlayCoinFlipperViewModel.GameState.BET -> {
            // Bet Form
            BetViewComposable(playCoinFlipperViewModel)
        }
        PlayCoinFlipperViewModel.GameState.PENDING -> {
            // Coin Flipping
            PendingViewComposable(playCoinFlipperViewModel)
        }
        PlayCoinFlipperViewModel.GameState.SETTLE -> {
            // Settle bet
            SettleViewComposable(playCoinFlipperViewModel)
        }
        else -> {
            // Default First View
            if (playCoinFlipperViewModel.hasExistingBet) {
                playCoinFlipperViewModel.hasExistingBetState()
            } else {
                // Bet Form
                BetViewComposable(playCoinFlipperViewModel)
            }
        }
    }
}

@Composable
fun BetViewComposable(playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
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
        playCoinFlipperViewModel.accountInfo?.let {
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
                imageVector = vectorResource(Res.drawable.coin_heads),
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
                            // ShowSnackbar(message = stringResource(Res.string.play_button_submit_bet))
                            Modifier.alpha(1.0f)
                        },
            )
            Image(
                imageVector = vectorResource(Res.drawable.coin_tails),
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
                            // ShowSnackbar(message = stringResource(Res.string.play_button_submit_bet))
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

@Composable
fun SettleViewComposable(playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
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
                // ShowSnackbar(message = stringResource(resource = Res.string.play_button_settle_wait))
            },
        )
    }
}

@Composable
fun ProgressComposable(playCoinFlipperViewModel: PlayCoinFlipperViewModel) {
    val currentRound by playCoinFlipperViewModel.currentRoundStateFlow.collectAsStateWithLifecycle()

    if (currentRound != null) {
        Text(
            stringResource(Res.string.play_current_round) + " ${playCoinFlipperViewModel.currentRound}",
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

fun getJsonDataFromAsset(
    context: Context,
    fileName: String,
): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
        Log.e("PlayCoinFlipperScreen", e.toString())
        return null
    }
    return jsonString
}
