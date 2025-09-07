package com.example.workschedule.Cloudinary

import android.content.Context
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback


object Cloudinary{
    private const val CLOUD_NAME = "dtzum2dzo"
    private const val API_KEY = "373418943822732"
    private const val API_SECRET = "sdLDlYBm7zau8htutbzlmQuzQOY"

    fun initialize(context: Context) {
        val config = HashMap<String, String>()
        config["cloud_name"] = CLOUD_NAME
        config["api_key"] = API_KEY
        config["api_secret"] = API_SECRET
        MediaManager.init(context, config)
    }

    fun uploadAudio(
        filePath: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
        onProgress: (Long, Long) -> Unit = { _, _ -> }
    ) {
        MediaManager.get().upload(filePath)
            .option("resource_type", "video") // Cloudinary xử lý audio như video
            .option("folder", "workschedule_audios")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String) {
                    // Upload bắt đầu
                }

                override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    onProgress(bytes, totalBytes)
                }

                override fun onSuccess(requestId: String, resultData: Map<Any?, Any?>) {
                    val url = resultData["url"] as? String
                    val secureUrl = resultData["secure_url"] as? String
                    onSuccess(secureUrl ?: url ?: "")
                }

                override fun onError(requestId: String, error: ErrorInfo) {
                    onError(error.description)
                }

                override fun onReschedule(requestId: String, error: ErrorInfo) {
                    onError(error.description)
                }
            })
            .dispatch()
    }
}