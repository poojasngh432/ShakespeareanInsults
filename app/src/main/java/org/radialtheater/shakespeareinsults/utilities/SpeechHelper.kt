package org.radialtheater.shakespeareinsults.utilities

import android.app.Activity
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.shared.SharedViewModel
import java.io.File
import java.util.*

/**
 * An object that speaks text
 */
class SpeechHelper(private val activity: Activity?) : TextToSpeech.OnInitListener {

    private val textToSpeech = TextToSpeech(activity, this)
    private var status: Int = TextToSpeech.ERROR
    private lateinit var outputFile: File

    // Speak the speech
    fun say(insult: String) {
        if (status != TextToSpeech.ERROR) {
            textToSpeech.speak(insult, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    // Create an audio file in app's cache directory
    fun createShareFile(sharer: InsultFileSharer, viewModel: SharedViewModel): Boolean {

        textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onDone(utteranceId: String?) {
                activity?.let { sharer.shareInsultFile(it, outputFile) }
            }

            override fun onError(utteranceId: String?) {
            }

            override fun onStart(utteranceId: String?) {
            }
        })

        val fileName = activity?.getString(
            R.string.share_file_name,
            viewModel.word1.value,
            viewModel.word2.value,
            viewModel.word3.value
        )
        outputFile = File(activity?.cacheDir, fileName!!)
        textToSpeech.synthesizeToFile(viewModel.insultForSharing, null, outputFile, "")

        return true
    }

    // Respond when file creation is complete
    override fun onInit(newStatus: Int) {
        status = newStatus
        textToSpeech.language = Locale.UK
    }

    // Interface with callback for calling class
    interface InsultFileSharer {
        fun shareInsultFile(activity: Activity, insultFile: File)
    }

}