package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.PictureOfTheDayRepository
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = PictureOfTheDayRepository(database)
        return try {
            repository.refreshPictureOfTheDay()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

}