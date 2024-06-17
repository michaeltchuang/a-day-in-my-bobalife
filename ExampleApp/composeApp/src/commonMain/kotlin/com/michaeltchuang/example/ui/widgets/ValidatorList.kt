package com.michaeltchuang.example.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import example_app.composeapp.generated.resources.Res
import example_app.composeapp.generated.resources.coin_heads
import example_app.composeapp.generated.resources.login_guest_message
import org.jetbrains.compose.resources.painterResource

@Composable
fun ValidatorCards(list: List<ValidatorEntity>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier.fillMaxSize(),
        content = {
            items(list.size) { index ->
                val data = list[index]
                ListItem(data)
            }
        },
    )
}

@Composable
fun ListItem(entity: ValidatorEntity) {
    Row(
        modifier =
            Modifier
                .height(100.dp)
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Column(
            modifier = Modifier.padding(end = 4.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.coin_heads),
                contentDescription = Res.string.login_guest_message.toString(),
                modifier =
                    Modifier
                        .wrapContentSize()
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .size(50.dp),
            )
        }
        Column(
            modifier = Modifier.padding(end = 4.dp),
        ) {
            Text(
                text = entity.name,
            )
        }
        Column(
            horizontalAlignment = Alignment.End,
            modifier =
                Modifier
                    .width(width = 100.dp),
        ) {
            Text(
                text = entity.apr.toString(),
            )
        }
    }
}

@Preview
@Composable
fun RowExample(
    validator: ValidatorEntity =
        ValidatorEntity(
            id = 1,
            name = "reti.algo",
            algoStaked = 25000,
            apr = 3.03,
            percentToValidator = 5,
            epochRoundLength = 1296,
            minEntryStake = 5,
        ),
) {
    ListItem(validator)
}
