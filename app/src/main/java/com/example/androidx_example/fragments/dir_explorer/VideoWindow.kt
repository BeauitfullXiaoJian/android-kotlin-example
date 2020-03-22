package com.example.androidx_example.fragments.dir_explorer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupWindow
import com.example.androidx_example.App
import com.example.androidx_example.R
import com.example.androidx_example.data.Video
import com.example.androidx_example.fragments.player.PlayerCtrlView
import com.example.androidx_example.until.ui.AnimateUntil
import com.example.httprequest.Request

class VideoWindow(
    file: DirExplorerViewModel.FileItem,
    rootView: ViewGroup
) :
    PopupWindow() {

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        contentView = LayoutInflater.from(rootView.context).inflate(
            R.layout.dir_explorer_video_window,
            rootView,
            false
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        AnimateUntil.popup(contentView)
        initVideoView(file)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun initVideoView(file: DirExplorerViewModel.FileItem) {
        val playerCtrlView = contentView.findViewById<PlayerCtrlView>(R.id.player_ctrl_view)
        playerCtrlView.apply {
            playerView = contentView.findViewById(R.id.player_view)
            playerImageView = contentView.findViewById(R.id.player_image_view)
            progressBar = contentView.findViewById(R.id.player_loading_bar)
            seekBar = contentView.findViewById(R.id.player_seek_bar)
            playTimeText = contentView.findViewById(R.id.player_time_text)
            playBtn = contentView.findViewById(R.id.player_btn_start)
            ctrlView = contentView.findViewById(R.id.player_ctrl_pad_view)
            prepareAndStart(
                video = Video(
                    id = 0,
                    videoTitle = file.filePath,
                    videoSourceUrl = Request.getRequestUrl("video?video=${file.downloadUrl}"),
                    videoThumbUrl = file.previewUrl
                ),
                recoverySeekValue = 0
            )
        }
    }

    companion object {
        fun createAndShow(file: DirExplorerViewModel.FileItem, rootView: ViewGroup) {
            VideoWindow(file, rootView)
                .showAtLocation(rootView, Gravity.CENTER, rootView.x.toInt(), rootView.y.toInt())
        }
    }
}