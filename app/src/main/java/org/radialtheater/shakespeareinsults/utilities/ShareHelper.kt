package org.radialtheater.shakespeareinsults.utilities

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import org.radialtheater.shakespeareinsults.LOG_TAG
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.displayToast
import org.radialtheater.shakespeareinsults.shared.SharedViewModel
import java.io.File

/**
 * A class that has functions to share insults as text, as an audio file
 * Also, a function to share the app with a friend
 */
class ShareHelper : SpeechHelper.InsultFileSharer {

    /**
     * Share an insult as text
     */
    fun shareInsultText(activity: Activity, viewModel: SharedViewModel): Boolean {
        ShareCompat.IntentBuilder
            .from(activity)
            .setType("text/plain")
            .setText(viewModel.insultForSharing)
            .setChooserTitle(R.string.share_as_text)
            .startChooser()
        return true
    }

    /**
     * Implementation of InsultFileSharer interface
     * Called after an audio file has been created in the app's cache
     */
    override fun shareInsultFile(activity: Activity, insultFile: File) {
        try {
            val contentUri: Uri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.provider",
                insultFile
            )
            ShareCompat.IntentBuilder
                .from(activity)
                .setType("audio/*")
                .setChooserTitle(R.string.share_as_file)
                .setStream(contentUri)
                .startChooser()
        } catch (e: Exception) {
            Log.i(LOG_TAG, e.message ?: "Unknown exception")
        }
    }

    /**
     * Send a message about the app to share it with a friend
     */
    fun shareApp(activity: Activity): Boolean {

        // Get the text of the message from a text file with raw HTML
        val htmlText = RawFileHelper.getRawText(activity, R.raw.share_app_body)

        // Build and start an intent to share the message. Android takes over from there.
        ShareCompat.IntentBuilder
            .from(activity)
            .setType("text/html")
            .setSubject("Insult your friends with this Android app")
            .setHtmlText(htmlText)
            .setChooserTitle("Share the Shakespearean Insult Generator")
            .startChooser()

        return true
    }

    /**
     *  Copy an insult to the clipboard
     */
    fun copyToClipboard(context: Context, viewModel: SharedViewModel): Boolean {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Your insult", viewModel.insultForSharing)
        clipboard.setPrimaryClip(clip)
        displayToast(context, R.string.copied_to_clipboard)
        return true
    }

}