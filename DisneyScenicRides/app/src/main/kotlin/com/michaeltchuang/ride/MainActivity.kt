package com.michaeltchuang.ride

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.michaeltchuang.ride.databinding.ActivityMainBinding
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener {
            val videoIntent = Intent(this@MainActivity, VideoPlayerActivity::class.java)
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
            videoIntent.putExtra(VideoPlayerActivity.VIDEO_YOUTUBE_ID_KEY, "u_MMCwORkrk") // DLP Avengers Campus Opening
            startActivity(videoIntent)
        }
    }
}