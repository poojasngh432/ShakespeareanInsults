package org.radialtheater.shakespeareinsults

import android.content.Context
import android.widget.Toast

@Suppress("unused")
const val LOG_TAG = "log_insults"

// A global function for displaying a Toast message
fun displayToast(context: Context, messageId: Int) {
    Toast.makeText(context, context.getString(messageId), Toast.LENGTH_LONG).show()
}
