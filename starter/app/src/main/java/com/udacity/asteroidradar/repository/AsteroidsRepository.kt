package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.getWeek
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.main.MainViewModel
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    fun getAsteroidsSelected(filter: MainViewModel.MenuItemOptions): LiveData<List<Asteroid>> {
        return when (filter) {
            MainViewModel.MenuItemOptions.Default ->
                Transformations.map(database.asteroidsDatabaseDao.getAsteroids()) {
                    it.asDomainModel()
                }

            MainViewModel.MenuItemOptions.Today ->
                Transformations.map(database.asteroidsDatabaseDao.getTodayAsteroids(getToday())) {
                    it.asDomainModel()
                }

            MainViewModel.MenuItemOptions.Week ->
                Transformations.map(database.asteroidsDatabaseDao.getWeekAsteroids(getToday(),
                    getWeek())) {
                    it.asDomainModel()
                }

            MainViewModel.MenuItemOptions.Saved ->
                Transformations.map(database.asteroidsDatabaseDao.getAsteroids()) {
                    it.asDomainModel()
                }
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidList =
                    AsteroidApi.retrofitServiceAsteroids.getAsteroids(getToday(),
                        getWeek())
                val parsedAsteroidList = parseAsteroidsJsonResult(JSONObject(asteroidList))
                database.asteroidsDatabaseDao.insertAll(*parsedAsteroidList.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class PictureOfTheDayRepository(private val database: AsteroidsDatabase) {

    val pictureOfTheDay: LiveData<PictureOfTheDay> =
        Transformations.map(database.pictureOfTheDayDatabaseDao.getPictureOfTheDay()) {
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfTheDay =
                    AsteroidApi.retrofitServicePictureOfTheDay.getPictureOfTheDay()
                database.pictureOfTheDayDatabaseDao.insertPicture(pictureOfTheDay.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}