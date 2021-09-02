package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
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

    private val _optionSelected = MutableLiveData<OptionSelected>()
    val optionSelected: LiveData<OptionSelected>
        get() = _optionSelected

    var asteroidOptionList = asteroidsRepository.asteroidsToday

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

    fun showOptionSelected(optionSelected: OptionSelected) {
        asteroidsList =
            Transformations.switchMap(_optionSelected) {
                when (it!!) {
                    OptionSelected.Today -> asteroidsRepository.asteroidsToday
                    OptionSelected.Week -> asteroidsRepository.asteroidsWeek
                    OptionSelected.Saved -> asteroidsRepository.asteroidSaved
                }
            }
    }

    var asteroidsList = asteroidsRepository.asteroidsToday

    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfTheDay
}


