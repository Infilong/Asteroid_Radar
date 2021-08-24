package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.getSevenDayAsteroids
import com.udacity.asteroidradar.api.getTodayAsteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.AsteroidApi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

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
    }

    private fun initAsteroids() {
        AsteroidApi.retrofitService.getAsteroids(getTodayAsteroid(),
            getSevenDayAsteroids(),
            Constants.API_KEY).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val jsonObject = response.body()?.string()
                if (jsonObject != null) {
                    _asteroids.value =
                        parseAsteroidsJsonResult(JSONObject(jsonObject))
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                _asteroids.value = null
            }
        })
    }
}

