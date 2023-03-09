package com.michaeltchuang.ride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.michaeltchuang.ride.databinding.ActivityMainBinding
import android.os.Bundle
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil

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
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Black)
                .padding(50.dp)
        ) {
            Button(
                onClick = {
                    if (onClick(R.string.play)) {
                        Modifier.alpha(1.0f)
                    } else {
                        Modifier.alpha(0.3f)
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.red)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .width(190.dp)
                    .height(50.dp)

            )
            {
                Text(
                    stringResource(R.string.play),
                    color = Color.White
                )
            }
            Image(
                painter = painterResource(id = R.drawable.xmas),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(R.drawable.xmas),
                        contentScale = ContentScale.FillWidth
                    )
            )
        }
    }

    fun onClick(resourceId: Int): Boolean {
        val videoIntent = Intent(this, VideoPlayerActivity::class.java)
        videoIntent.putExtra(VideoPlayerActivity.VIDEO_TITLE_KEY, "Disney Scenic Ride")
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "n4tWT-Tzff0") //incredible hulk
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "IiV06MMsMW8") //incredible hulk - Stan Lee
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "rcBzXxW-aj4") //spider-man: homecoming
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "7l4a73ruC4c") //spider-man: homecoming - Stan Lee
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "FboDbmhiOq8") //spider-man: far from home
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "QdpxoFcdORI") //celebrates the movies (shang chi)
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "giWIr7U1deA") //shang chi teaser trailer
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "YjFbMbfXaQ") //shang chi trailer
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "uPY9K5Xs8Xc") //dlp
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "ZVN3ze7l0XM") //d23 2018
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "orrFo_lBLH4") //SHDL Marvel
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "XLnrePnk04Q") //harminous 8k
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "fLsDpcBsScg") //hkdl momentous
        //videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "A4kZ2Nnsm_g") // spiderman: no way home
        videoIntent.putExtra(
            VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY,
            "u_MMCwORkrk"
        ) // DLP Avengers Campus Opening
        startActivity(videoIntent)
        return true
    }
}
