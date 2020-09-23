package com.arya.mymoviedb.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.arya.mymoviedb.model.TvResult

@Dao
interface FavTvDao {
    @Insert
    suspend fun saveTv(favorite: TvResult)

    @Query("select * from TvResult")
    suspend fun readTv(): List<TvResult>

    @Delete
    suspend fun delTv(favorite: TvResult)

    @Query("select exists (select 1 from tvresult where id = :id)")
    suspend fun isFavoriteTv(id: Int): Int
}