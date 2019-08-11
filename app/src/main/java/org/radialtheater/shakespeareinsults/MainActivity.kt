package org.radialtheater.shakespeareinsults

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Not much here, everything we need is in the activity's layout
    // or in nested fragments managed by the Navigation controller
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
