package com.arya.mymoviedb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arya.mymoviedb.model.MovieResult

@Database (entities = [(MovieResult::class)], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class FavMovieDB: RoomDatabase() {
    abstract fun favMovieDAO(): FavMovieDao

    companion object {
        @Volatile private var instance: FavMovieDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            FavMovieDB::class.java,
            "FavoriteMovie"
        ).build()
    }
}