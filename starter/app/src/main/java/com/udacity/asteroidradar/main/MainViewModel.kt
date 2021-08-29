package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val database = getDatabase(app)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val pictureOfTheDayRepository = PictureOfTheDayRepository(database)

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: LiveData<Asteroid?>
        get() = _navigateToDetailFragment

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun navigationReset() {
        _navigateToDetailFragment.value = null
    }

    init {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
                pictureOfTheDayRepository.refreshPictureOfTheDay()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


