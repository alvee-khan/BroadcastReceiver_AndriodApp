package com.example.broadcastreceiver

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class VideoFragment : Fragment() {

    private var videoView: VideoView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_video,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val video = view.findViewById<VideoView>(
            R.id.videoView
        )

        videoView = video

        val playButton = view.findViewById<MaterialButton>(
            R.id.btnPlayVideo
        )

        val statusText = view.findViewById<TextView>(
            R.id.txtVideoStatus
        )

        val mediaController = MediaController(requireContext())

        mediaController.setAnchorView(video)

        video.setMediaController(mediaController)

        val videoUri = Uri.parse(
            "android.resource://${requireContext().packageName}/${R.raw.sample_video}"
        )

        video.setVideoURI(videoUri)

        video.setOnPreparedListener {
            statusText.text = "Video ready to play"
        }

        video.setOnCompletionListener {
            statusText.text = "Video finished"
        }

        video.setOnErrorListener { _, _, _ ->
            statusText.text = "Unable to play this video"
            true
        }

        playButton.setOnClickListener {

            video.start()

            statusText.text = "Playing video"
        }
    }

    override fun onDestroyView() {

        videoView?.stopPlayback()
        videoView = null

        super.onDestroyView()
    }
}
