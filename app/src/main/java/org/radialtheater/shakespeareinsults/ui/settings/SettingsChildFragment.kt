package org.radialtheater.shakespeareinsults.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import org.radialtheater.shakespeareinsults.R

/**
 * Load preferences screen
 * This is nested within a parent fragment
 */
class SettingsChildFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_settings)
    }

}
