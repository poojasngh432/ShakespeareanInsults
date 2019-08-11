package org.radialtheater.shakespeareinsults.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.radialtheater.shakespeareinsults.R
import org.radialtheater.shakespeareinsults.db.Insult
import org.radialtheater.shakespeareinsults.db.InsultRepository
import org.radialtheater.shakespeareinsults.utilities.RawFileHelper
import org.radialtheater.shakespeareinsults.utilities.SoundHelper
import org.radialtheater.shakespeareinsults.utilities.startsWithVowel
import kotlin.random.Random

const val SUSPEND_DURATION = 100L

class SharedViewModel(val app: Application) : AndroidViewModel(app) {

    // repository to work with persistent data
    private val repository = InsultRepository(app)

    // helper object to manage sounds
    private var soundHelper = SoundHelper(app)

    //  3 arrays of words from JSON resource file
    private val insultWords: Array<Array<String>> = RawFileHelper.getRawData(app)

    //  randomized words
    val word1 = MutableLiveData<String>()
    val word2 = MutableLiveData<String>()
    val word3 = MutableLiveData<String>()

    //  current insult
    val insult = MutableLiveData<String>()
    val insultForSharing: String?
        get() = insult.value?.replace("\n", " ")

    // data set for "last 10" screen
    val last10Insults: LiveData<List<Insult>>

    // boolean values to manage UI state
    val buttonEnabled = MutableLiveData<Boolean>()
    var generatingInsult = MutableLiveData<Boolean>()

    /**
     * Set initial values
     */
    init {
        // init UI state
        buttonEnabled.value = true
        generatingInsult.value = false
        word1.value = app.getString(R.string.word1_default)
        word2.value = app.getString(R.string.word2_default)
        word3.value = app.getString(R.string.word3_default)

        // init data set for "last 10" screen
        last10Insults = repository.last10Insults
    }

    /**
     * Generate a new insult and set it in the LiveData object
     */
    fun generateInsult() {

        // insultNow = false
        generatingInsult.value = true

        // Send message to UI to disable button
        buttonEnabled.value = false

        // play sound - any conditions or exceptions are handled in the helper object
        soundHelper.playCalculatingSound()

        // Word 1 - 2 seconds
        viewModelScope.launch {
            for (i in 1..20) {
                delay(SUSPEND_DURATION)
                val wordIndex = Random.nextInt(0, insultWords[0].size)
                word1.value = insultWords[0][wordIndex]
            }
        }

        // Word 2 - 3 seconds
        viewModelScope.launch {
            for (i in 1..30) {
                delay(SUSPEND_DURATION)
                val wordIndex = Random.nextInt(0, insultWords[1].size)
                word2.value = insultWords[1][wordIndex]
            }
        }

        // Word 3 - 4 seconds, then generate full insult
        viewModelScope.launch {
            for (i in 1..40) {
                delay(SUSPEND_DURATION)
                val wordIndex = Random.nextInt(0, insultWords[2].size)
                word3.value = insultWords[2][wordIndex]
            }

            // stop the audio
            soundHelper.stopCalculatingSound()

            // Get an article: "an" or "a"
            val article = if (word1.value!!.startsWithVowel())
                "an"
            else
                "a"

            // Generate the insult
            delay(100)
            insult.value = "Thou art $article\n${word1.value}\n${word2.value}\n${word3.value}!"
            generatingInsult.value = false

            // Save the new insult to persistent storage
            val insultObj = Insult(insult.value!!)
            repository.insertInsult(insultObj)

        }

    }

    /**
     * Stop insult generation
     */
    fun stopGeneratingInsult() {
        soundHelper.stopCalculatingSound()
    }

}
