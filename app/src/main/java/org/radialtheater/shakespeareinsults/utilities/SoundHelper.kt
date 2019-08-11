package org.radialtheater.shakespeareinsults.utilities

import android.app.Application
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import androidx.preference.PreferenceManager
import org.radialtheater.shakespeareinsults.R

const val SOUND_PREF_KEY = "play_sounds"

class SoundHelper(app: Application) {

    // shared preferences object
    private val prefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(app)

    // variables for managing sounds
    private var soundPool: SoundPool
    private var calcSoundId = 0
    private var calcStreamId = 0
    private var calcSoundLoaded = false

    init {
        // init sounds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .build()
        } else {
            @Suppress("DEPRECATION")
            soundPool = SoundPool(5, AudioManager.STREAM_MUSIC, 0)
        }
        soundPool.setOnLoadCompleteListener { _, _, status ->
            calcSoundLoaded = (status == 0)
        }
        calcSoundId = soundPool.load(app, R.raw.calculate_sound, 1)
    }

    // play the calculating sound
    fun playCalculatingSound() {
        val soundPref = prefs.getBoolean(SOUND_PREF_KEY, true)
        if (soundPref && calcSoundLoaded) {
            calcStreamId = soundPool.play(
                calcSoundId, 1f, 1f,
                0, 1, 1f
            )
        }
    }

    // stop the calculating sound
    fun stopCalculatingSound() {
        if ((calcStreamId == 0).not()) {
            soundPool.stop(calcStreamId)
        }
    }
}