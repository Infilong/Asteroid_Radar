package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay

// converts from data transfer objects to database objects
fun List<Asteroid>.asDatabaseModel(): Array<AsteroidEntity> {
    return map {
        AsteroidEntity(
            id = it.id,
            codeName = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun PictureOfTheDay.asDatabaseModel(): PictureOfTheDayEntity {
    return (
            PictureOfTheDayEntity(
                url = this.url,
                mediaType = this.mediaType,
                title = this.title)
            )
}