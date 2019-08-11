package org.radialtheater.shakespeareinsults.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

const val DB_NAME = "insultDB"

@Database(entities = [Insult::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class InsultDatabase : RoomDatabase() {

    abstract fun insultDao(): InsultDao

    companion object {

        @Volatile
        private var INSTANCE: InsultDatabase? = null

        fun getDatabase(context: Context): InsultDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    InsultDatabase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}