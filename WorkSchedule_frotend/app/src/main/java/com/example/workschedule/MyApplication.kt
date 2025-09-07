package com.example.workschedule

import android.app.Application
import com.example.workschedule.Cloudinary.Cloudinary

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Cloudinary.initialize(this)
    }
}