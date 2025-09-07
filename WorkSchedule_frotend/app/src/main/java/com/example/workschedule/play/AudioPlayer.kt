package com.example.workschedule.play

import android.media.MediaPlayer
import android.net.Uri
import java.io.IOException

class AudioPlayer {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    fun playAudio(uri: Uri, onCompletion: () -> Unit = {}) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(uri.path) // Lấy path từ URI
                prepare()
                start()
                setOnCompletionListener {
                    onCompletion()
                    stopAudio()
                }
            }
            isPlaying = true
        } catch (e: Exception) {
            e.printStackTrace()
            onCompletion()
        }
    }

    fun stopAudio() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    fun isPlaying(): Boolean = isPlaying

    fun playAudioFromUrl(url: String, onCompletion: () -> Unit = {}) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                prepareAsync()
                setOnPreparedListener { start() }
                setOnCompletionListener {
                    onCompletion()
                    stopAudio()
                }
                setOnErrorListener { _, what, extra ->
                    onCompletion()
                    false
                }
            }
            isPlaying = true
        } catch (e: IOException) {
            e.printStackTrace()
            onCompletion()
        }
    }
}