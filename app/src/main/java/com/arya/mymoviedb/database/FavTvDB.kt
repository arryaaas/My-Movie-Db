package com.arya.mymoviedb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arya.mymoviedb.model.TvResult

@Database(entities = [(TvResult::class)], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class FavTvDB: RoomDatabase() {
    abstract fun favTvDAO(): FavTvDao

    companion object {
        @Volatile private var instance: FavTvDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FavTvDB::class.java,
            "FavoriteTv"
        ).build()
    }
}