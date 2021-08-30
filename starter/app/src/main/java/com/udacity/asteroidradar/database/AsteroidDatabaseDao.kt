package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabaseDao {
    @Query("SELECT * FROM daily_asteroid_info_table ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM daily_asteroid_info_table WHERE closeApproachDate=:today")
    fun getTodayAsteroids(today: Long): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM daily_asteroid_info_table WHERE closeApproachDate BETWEEN :today AND :sevenDay ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(today: Long, sevenDay: Long): LiveData<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: AsteroidEntity)
}

@Dao
interface PictureOfTheDayDatabaseDao {
    @Query("SELECT * FROM picture_of_the_day_table")
    fun getPictureOfTheDay(): LiveData<PictureOfTheDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(pictureOfTheDay: PictureOfTheDayEntity)
}

@Database(entities = [AsteroidEntity::class, PictureOfTheDayEntity::class],
    version = 1,
    exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidsDatabaseDao: AsteroidDatabaseDao
    abstract val pictureOfTheDayDatabaseDao: PictureOfTheDayDatabaseDao
}

private lateinit var INSTANCE: AsteroidsDatabase
fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE =
                Room.databaseBuilder(context.applicationContext,
                    AsteroidsDatabase::class.java,
                    "asteroids")
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
    return INSTANCE
}


