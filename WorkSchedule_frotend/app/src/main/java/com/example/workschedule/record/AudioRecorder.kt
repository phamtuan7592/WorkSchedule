package com.example.workschedule.record

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AudioRecorder(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var outputUri: Uri? = null
    private var isRecording = false

    fun startRecording(): Uri? {
        if (isRecording) return outputUri

        try {
            mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                MediaRecorder()
            }

            val audioFile = createAudioFile()
            outputUri = Uri.fromFile(audioFile)

            mediaRecorder?.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile.absolutePath)
                setAudioEncodingBitRate(128000)
                setAudioSamplingRate(44100)

                prepare()
                start()
                isRecording = true
            }

            return outputUri
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            return null
        }
    }

    fun stopRecording() {
        if (!isRecording) return

        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaRecorder = null
            isRecording = false
        }
    }

    fun isRecording(): Boolean = isRecording

    fun getRecordedUri(): Uri? = outputUri

    fun deleteRecordedFile(): Boolean {
        return try {
            outputUri?.path?.let { filePath ->
                val file = File(filePath)
                if (file.exists()) {
                    file.delete()
                } else {
                    false
                }
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun createAudioFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

        val cacheDir = context.cacheDir
        val recordingsDir = File(cacheDir, "recordings")


        if (!recordingsDir.exists()) {
            recordingsDir.mkdirs()
        }

        return File.createTempFile(
            "AUDIO_${timeStamp}_",
            ".mp4",
            recordingsDir
        )
    }
}