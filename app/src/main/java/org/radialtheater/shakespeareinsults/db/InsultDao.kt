package org.radialtheater.shakespeareinsults.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InsultDao {

    @Query("SELECT * from insults ORDER BY timeStamp DESC LIMIT 10")
    fun getLast10(): LiveData<List<Insult>>

    @Insert
    suspend fun insertInsult(insult: Insult)

}