package com.podplay.android.data.db

import android.content.Context
import androidx.room.*
import com.podplay.android.data.model.Episode
import com.podplay.android.data.model.Podcast
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?) : Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun toTimeStamp(date: Date?) : Long? {
        return (date?.time)
    }
}

@Database(entities = [Podcast::class, Episode::class], version = 3)
@TypeConverters(Converters::class)
abstract class PodPlayDatabase : RoomDatabase() {

    abstract fun podcastDao(): PodcastDao

    companion object {
        @Volatile
        private var INSTANCE: PodPlayDatabase? = null

        fun getInstance(context: Context): PodPlayDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PodPlayDatabase::class.java,
                    "PodPlayer",
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
