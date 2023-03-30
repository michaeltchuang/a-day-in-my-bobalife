package com.michaeltchuang.ride.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.michaeltchuang.ride.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import com.michaeltchuang.ride.R
import com.michaeltchuang.ride.data.DataProvider
import com.michaeltchuang.ride.data.DataProvider.videos
import com.michaeltchuang.ride.data.models.Video

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding?>(this, R.layout.activity_main)
            .apply {
                composeActivityMain.setContent {
                    MainActivityComposable()
                }
            }
    }

    @Composable
    fun MainActivityComposable() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .padding(50.dp)
        ) {
            VideoHub()
            Row(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.xmas),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .paint(
                            painter = painterResource(R.drawable.xmas),
                            contentScale = ContentScale.Fit
                        )
                )
            }

        }
    }

    @Composable
    fun VideoHub() {
        val videos = remember { DataProvider.videos }
        LazyRow(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(space = 24.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(
                items = videos,
                itemContent = {
                    VideoItem(video = it)
                })
        }
    }

    @Composable
    fun VideoItem(video: Video) {
        Button(
            onClick = {
                if (onClick(video.youtubeId)) {
                    Modifier.alpha(1.0f)
                } else {
                    Modifier.alpha(0.3f)
                }
            },
            colors = ButtonDefaults.buttonColors(colorResource(R.color.red)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .width(250.dp)
                .height(50.dp)

        )
        {
            Text(
                video.title,
                color = Color.White
            )
        }
    }

    fun onClick(youtubeVideoId: String): Boolean {
        val videoIntent = Intent(this, VideoPlayerActivity::class.java)
        videoIntent.putExtra(VideoPlayerActivity.VIDEO_TITLE_KEY, "Disney Scenic Ride")
        videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, youtubeVideoId)
        startActivity(videoIntent)
        return true
    }
}
