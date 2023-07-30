package com.example.docmate

import android.app.Application
import com.google.firebase.FirebaseApp
//import com.google.firebase.analytics.FirebaseAnalytics
//import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DocMateClass:Application() {
    override fun onCreate() {
        super.onCreate()

        // Enable automatic data collection
//        FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true)
//            FirebaseApp.initializeApp(this)
    }
}