package org.radialtheater.shakespeareinsults.ui.about

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.radialtheater.shakespeareinsults.BuildConfig
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.utilities.RawFileHelper
import java.text.DateFormat
import java.util.*

/**
 * This ViewModel is only for the About fragment
 */
class AboutViewModel(val app: Application) : AndroidViewModel(app) {

    val aboutText = MutableLiveData<String>()

    init {

        // Get package, build date into variables
        val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
        val pInfo = app.packageManager.getPackageInfo(app.packageName, 0)
        val buildDate = df.format(Date(BuildConfig.TIMESTAMP))

        // Build text values for data binding
        val rawText = RawFileHelper.getRawText(app, R.raw.about_text)
        aboutText.value = String.format(rawText, pInfo.versionName, buildDate)

    }

}
