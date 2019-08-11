package org.radialtheater.shakespeareinsults.utilities

import android.content.Context
import androidx.preference.PreferenceManager

private const val GRADIENT_HEIGHT = "gradient_height"

class PreferenceHelper(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun setGradientHeight(height: Int) {
        with(prefs.edit()) {
            putInt(GRADIENT_HEIGHT, height)
            apply()
        }
    }

    fun getGradientHeight(): Int {
        return prefs.getInt(GRADIENT_HEIGHT, 200)
    }
}