package org.radialtheater.shakespeareinsults.utilities

import android.content.Context
import com.google.gson.Gson
import org.radialtheater.shakespeareinsults.R
import java.io.InputStreamReader

class RawFileHelper {

    companion object {
        fun getRawData(context: Context): Array<Array<String>> {

            val inputStream = context.resources.openRawResource(R.raw.insult_words)
            inputStream.use {
                InputStreamReader(inputStream).use {
                    return Gson().fromJson(it, Array<Array<String>>::class.java)
                }
            }

        }

        fun getRawText(context: Context, resId: Int): String {
            val inputStream = context.resources.openRawResource(resId)
            inputStream.use {
                return it.readBytes().toString(Charsets.UTF_8)
            }
        }
    }

}