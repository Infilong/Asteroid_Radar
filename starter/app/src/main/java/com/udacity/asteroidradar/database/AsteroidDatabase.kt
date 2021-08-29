package com.udacity.asteroidradar.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDatabaseDao: AsteroidDatabaseDao
    abstract val pictureOfTheDayDatabaseDao: PictureOfTheDayDatabaseDao
}
