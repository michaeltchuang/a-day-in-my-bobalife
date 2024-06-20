package com.michaeltchuang.example.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.ui.theme.md_theme_light_onPrimary
import com.michaeltchuang.example.ui.theme.md_theme_light_primary
import com.michaeltchuang.example.ui.theme.md_theme_light_primaryContainer
import com.michaeltchuang.example.utils.truncatedValidatorName
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.ic_logo
import example_app.composeapp.generated.resources.validator_icon_content_description
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@SuppressLint("ComposableNaming")
@Composable
fun ValidatorSearchListItem(entity: ValidatorEntity) {
    Row(
        modifier =
            Modifier
                .height(100.dp)
                .fillMaxWidth()
                .padding(5.dp)
                .background(color = md_theme_light_primary, shape = RoundedCornerShape(20.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier =
                Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
        ) {
//            AsyncImage(
//                model = Res.drawable.ic_logo,
// //                model = ImageRequest.Builder(LocalContext.current)
// //                    .data("https://example.com/image.jpg")
// //                    .crossfade(true)
// //                    .build(),
//                placeholder = painterResource(Res.drawable.ic_logo),
//                contentDescription = stringResource(Res.string.validator_icon_content_description),
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .wrapContentSize()
//                    .wrapContentHeight()
//                    .wrapContentWidth()
//                    .size(50.dp)
//            )
            Image(
                painter = painterResource(Res.drawable.ic_logo),
                contentDescription = stringResource(Res.string.validator_icon_content_description),
                modifier =
                    Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .clip(CircleShape),
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier =
                Modifier
                    .padding(start = 15.dp, end = 15.dp)
                    .fillMaxHeight()
                    .weight(1f),
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimary,
                text = truncatedValidatorName(entity.name),
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primaryContainer,
                text =
                    when (entity.algoStaked) {
                        null -> "---"
                        else -> "${entity.algoStaked} ALGO Staked"
                    },
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier =
                Modifier
                    .fillMaxHeight()
                    .width(75.dp),
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                color = md_theme_light_onPrimary,
                modifier =
                    Modifier
                        .padding(end = 25.dp),
                text =
                    when (entity.apy) {
                        null -> "---"
                        else -> "${entity.apy}%"
                    },
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                fontWeight = FontWeight.Bold,
                color = md_theme_light_primaryContainer,
                text = "",
            )
        }
    }
}

@Preview
@SuppressLint("ComposableNaming")
@Composable
fun ValidatorListItemExample(
    validator: ValidatorEntity =
        ValidatorEntity(
            id = 1,
            name = "reti.algo",
            algoStaked = 25000,
            apy = 3.03,
            percentToValidator = 5,
            epochRoundLength = 1296,
            minEntryStake = 5,
        ),
) {
    ValidatorSearchListItem(validator)
}
