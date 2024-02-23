package com.example.petfinder

import android.app.Application
import android.util.Log
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp
import st.lowlevel.storo.BuildConfig
import st.lowlevel.storo.StoroBuilder
import timber.log.Timber

@HiltAndroidApp
class PetfinderApplication : Application() {

    val CACHE_MANAGER_MAX_STORAGE = (40 * 1024 * 1024).toLong()
    override fun onCreate() {
        super.onCreate()
        loggingActivation()
        setupStoro()
    }

    private fun loggingActivation() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
        Timber.i("Application logging activated")
    }

    private fun setupStoro() {
        StoroBuilder.configure(CACHE_MANAGER_MAX_STORAGE) // maximum size to allocate in bytes
            .setCacheDirectory(filesDir)
            .setGsonInstance(GsonBuilder().create())
            .initialize()
    }


    private class CrashReportingTree : Timber.Tree() {
        protected override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }
            FakeCrashLibrary.log(priority, tag, message)
            if (t != null) {
                if (priority == Log.ERROR) {
                    FakeCrashLibrary.logError(t)
                } else if (priority == Log.WARN) {
                    FakeCrashLibrary.logWarning(t)
                }
            }
        }
    }

    internal class FakeCrashLibrary private constructor() {
        init {
            throw AssertionError("No instances.")
        }

        companion object {
            fun log(priority: Int, tag: String?, message: String?) {
                // TODO add log entry to circular buffer.
            }

            fun logWarning(t: Throwable?) {
                // TODO report non-fatal warning.
            }

            fun logError(t: Throwable?) {
                // TODO report non-fatal error.
            }
        }
    }

}