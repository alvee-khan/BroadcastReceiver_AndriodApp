package com.example.broadcastreceiver

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class AudioFragment : Fragment() {

    private var mediaPlayer: MediaPlayer? = null

    private lateinit var audioSeekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var totalTimeText: TextView
    private lateinit var statusText: TextView

    private val handler = Handler(Looper.getMainLooper())

    private var isSeeking = false

    // Update the slider while audio is playing.
    private val progressUpdater = object : Runnable {

        override fun run() {

            mediaPlayer?.let { player ->

                if (!isSeeking) {

                    val position = player.currentPosition

                    audioSeekBar.progress = position

                    currentTimeText.text = formatTime(position)
                }

                handler.postDelayed(this, 300)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.fragment_audio,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<MaterialButton>(
            R.id.btnPlayAudio
        )

        val pauseButton = view.findViewById<MaterialButton>(
            R.id.btnPauseAudio
        )

        val stopButton = view.findViewById<MaterialButton>(
            R.id.btnStopAudio
        )

        statusText = view.findViewById(
            R.id.txtAudioStatus
        )

        audioSeekBar = view.findViewById(
            R.id.audioSeekBar
        )

        currentTimeText = view.findViewById(
            R.id.txtCurrentTime
        )

        totalTimeText = view.findViewById(
            R.id.txtTotalTime
        )

        // Load audio file.
        mediaPlayer = MediaPlayer.create(
            requireContext(),
            R.raw.sample_audio
        )

        val player = mediaPlayer

        if (player == null) {

            statusText.text = "Unable to load audio"

            audioSeekBar.isEnabled = false
            playButton.isEnabled = false
            pauseButton.isEnabled = false
            stopButton.isEnabled = false

            return
        }

        // Set the slider maximum to the audio duration.
        val duration = player.duration.coerceAtLeast(0)

        audioSeekBar.max = duration

        totalTimeText.text = formatTime(duration)
        currentTimeText.text = formatTime(0)

        // PLAY
        playButton.setOnClickListener {

            player.start()

            statusText.text = "Playing audio"
        }

        // PAUSE
        pauseButton.setOnClickListener {

            if (player.isPlaying) {

                player.pause()

                statusText.text = "Audio paused"
            }
        }

        // STOP
        stopButton.setOnClickListener {

            if (player.isPlaying) {
                player.pause()
            }

            player.seekTo(0)

            audioSeekBar.progress = 0

            currentTimeText.text = formatTime(0)

            statusText.text = "Audio stopped"
        }

        // SEEK BAR
        audioSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {

                    if (fromUser) {

                        currentTimeText.text = formatTime(progress)
                    }
                }

                override fun onStartTrackingTouch(
                    seekBar: SeekBar?
                ) {

                    isSeeking = true
                }

                override fun onStopTrackingTouch(
                    seekBar: SeekBar?
                ) {

                    if (seekBar != null) {

                        player.seekTo(seekBar.progress)

                        currentTimeText.text = formatTime(
                            seekBar.progress
                        )
                    }

                    isSeeking = false
                }
            }
        )

        // AUDIO COMPLETED
        player.setOnCompletionListener {

            audioSeekBar.progress = duration

            currentTimeText.text = formatTime(duration)

            statusText.text = "Audio finished"
        }
    }

    override fun onStart() {
        super.onStart()

        if (mediaPlayer != null) {

            handler.removeCallbacks(progressUpdater)
            handler.post(progressUpdater)
        }
    }

    override fun onStop() {

        handler.removeCallbacks(progressUpdater)

        mediaPlayer?.let { player ->

            if (player.isPlaying) {

                player.pause()

                statusText.text = "Audio paused"
            }
        }

        super.onStop()
    }

    override fun onDestroyView() {

        handler.removeCallbacks(progressUpdater)

        mediaPlayer?.release()
        mediaPlayer = null

        super.onDestroyView()
    }

    // Convert milliseconds to minutes:seconds.
    private fun formatTime(milliseconds: Int): String {

        val totalSeconds = milliseconds / 1000

        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }
}
