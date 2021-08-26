package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay
import com.udacity.asteroidradar.api.getSevenDayAsteroids
import com.udacity.asteroidradar.api.getTodayAsteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel : ViewModel() {
    private val _statusPictureOfTheDay = MutableLiveData<String>()
    val statusPictureOfTheDay: LiveData<String>
        get() = _statusPictureOfTheDay

    private val _statusAsteroids = MutableLiveData<String>()
    val statusAsteroids: LiveData<String>
        get() = _statusAsteroids

    private val _pictureOfTheDay = MutableLiveData<PictureOfTheDay>()
    val pictureOfTheDay: LiveData<PictureOfTheDay>
        get() = _pictureOfTheDay

    private val _asteroids = MutableLiveData<List<Asteroid>?>()
    val asteroids: MutableLiveData<List<Asteroid>?>
        get() = _asteroids

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
        initAsteroids()
        initTheImageOfTheDay()
    }

    private fun initAsteroids() {
        viewModelScope.launch {
            try {
                val asteroidsList = AsteroidApi.retrofitServiceAsteroids
                    .getAsteroids(getTodayAsteroid(), getSevenDayAsteroids())
                _asteroids.value =
                    parseAsteroidsJsonResult(JSONObject(asteroidsList))
            } catch (e: Exception) {
                _asteroids.value = null
                _statusAsteroids.value = "Failure: ${e.message}"
            }
        }
//        AsteroidApi.retrofitServiceAsteroids
//            .getAsteroids(getTodayAsteroid(), getSevenDayAsteroids(),
//                Constants.API_KEY).enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>,
//                ) {
//                    val jsonObject = response.body()?.string()
//                    if (jsonObject != null) {
//                        _asteroids.value =
//                            parseAsteroidsJsonResult(JSONObject(jsonObject))
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    _asteroids.value = null
//                }
//            })
    }

    private fun initTheImageOfTheDay() {
        viewModelScope.launch {
            try {
                _pictureOfTheDay.value =
                    AsteroidApi.retrofitServicePictureOfTheDayOfTheDay.getPictureOfTheDay()
            } catch (e: Exception) {
                _statusPictureOfTheDay.value = "Failure: ${e.message}"
            }
        }

    }
}


