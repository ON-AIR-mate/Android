package umc.onairmate

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import android.content.Context
import androidx.annotation.StringRes

@HiltAndroidApp
class OnAirMateApplication : Application(), DefaultLifecycleObserver {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getString(@StringRes stringResId: Int): String{
            return context.getString(stringResId)
        }

        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: OnAirMateApplication
        fun applicationContext(): Context{
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super<Application>.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        context = applicationContext
    }
}