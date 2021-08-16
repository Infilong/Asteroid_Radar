package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface AsteroidDatabaseDao {
    @Query("SELECT * FROM daily_asteroid_info_table ORDER BY id DESC LIMIT 1")
    suspend fun getTodayAsteroid(): AsteroidEntities?

    @Query("SELECT * FROM daily_asteroid_info_table WHERE id=:key")
    fun getAsteroidWithId(key: Long): LiveData<AsteroidEntities>
}