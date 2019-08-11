package org.radialtheater.shakespeareinsults.db

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class InsultRepository(app: Application) {

    // DAO to manage insults table in SQLite
    private val insultDao = InsultDatabase.getDatabase(app).insultDao()

    // data set for "last 10" fragment
    val last10Insults: LiveData<List<Insult>> = insultDao.getLast10()

    /**
     * Insert a new insult
     */
    @WorkerThread
    suspend fun insertInsult(insult: Insult) {
        insultDao.insertInsult(insult)
    }
}