package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : AndroidViewModel(app) {
    enum class MenuItemOptions { Default, Today, Week, Saved }

    private val asteroidOption = MutableLiveData<MenuItemOptions>()
    val asteroidList = Transformations.switchMap(asteroidOption) { menuOption ->
        asteroidsRepository.getAsteroidsSelected(menuOption)
    }
    private val database = getDatabase(app)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val pictureOfTheDayRepository = PictureOfTheDayRepository(database)

    private val _navigateToDetailFragment = MutableLiveData<Asteroid?>()
    val navigateToDetailFragment: LiveData<Asteroid?>
        get() = _navigateToDetailFragment

    init {
        asteroidOption.postValue(MenuItemOptions.Default)
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
                pictureOfTheDayRepository.refreshPictureOfTheDay()
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }

    fun updateAsteroidOptionList(option: MenuItemOptions) {
        asteroidOption.postValue(option)
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    fun navigationReset() {
        _navigateToDetailFragment.value = null
    }

    init {
        asteroidOption.postValue(MenuItemOptions.Default)
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            pictureOfTheDayRepository.refreshPictureOfTheDay()

        }
    }

    val pictureOfTheDay = pictureOfTheDayRepository.pictureOfTheDay
}


