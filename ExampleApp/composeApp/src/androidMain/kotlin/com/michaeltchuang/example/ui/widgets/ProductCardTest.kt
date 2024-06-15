package com.michaeltchuang.example.ui.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun ProductItemTest(
    name: String,
    description: String,
    price: String,
    discount: String,
    image: String,
) {
    Card(
        modifier =
            Modifier.padding(8.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface, // Card background color
                // contentColor = Color.White  //Card content color,e.g.text
            ),
    ) {
        Column(modifier = Modifier.height(380.dp).padding(10.dp)) {
            KamelImage(
                resource = asyncPainterResource(data = image),
                contentDescription = null,
                modifier = Modifier.height(150.dp).fillMaxWidth().clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = name,
                    maxLines = 3,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = description,
                    fontWeight = FontWeight.Normal,
                    maxLines = 4,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "$$price",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = "$discount% discount",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(red = 0.1f, green = 0.8f, blue = 0.0f),
                )
            }
        }
    }
}
