package org.radialtheater.shakespeareinsults.db

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "insults")
data class Insult(
    @PrimaryKey(autoGenerate = true)
    var insultKey: Int,
    var insultText: String,
    var timeStamp: Date
) {

    /**
     * Constructor just receives insult text
     * Primary key is generated by database
     * Time stamp is generated here
     */
    @Ignore
    constructor(insultText: String) : this(0, insultText, Date())

}