package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _asteroidProperties = MutableLiveData<Asteroid>()
    val asteroidProperties: LiveData<Asteroid>
        get() = _asteroidProperties

    init {
        getAsteroidProperties()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _asteroidProperties.value = asteroid
    }

    private fun getAsteroidProperties() {
//        AsteroidApi.retrofitService.getProperties().enqueue(object : Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                _status.value = "Failure " + t.message
//            }
//
//            override fun onResponse(
//                call: Call<String>,
//                response: Response<String>,
//            ) {
//                _status.value = "Success: ${response.body()} Asteroid properties retrieved"
//            }
//        })
        viewModelScope.launch {
            try {
                var listResult = AsteroidApi.retrofitService.getProperties()
                _status.value = "Success: ${listResult.size} Mars properties retrieved"
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }
}

