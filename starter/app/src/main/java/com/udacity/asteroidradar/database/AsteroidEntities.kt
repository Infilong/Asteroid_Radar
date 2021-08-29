package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay

@Entity(tableName = "daily_asteroid_info_table")
data class AsteroidEntity(
    @PrimaryKey()
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
)

//converts from database objects to domain objects
fun List<AsteroidEntity>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codeName = it.codeName,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

@Entity(tableName = "picture_of_the_day_table")
data class PictureOfTheDayEntity(
    @PrimaryKey()
    val url: String,
    val mediaType: String,
    val title: String,
)

fun PictureOfTheDayEntity.asDomainModel(): PictureOfTheDay {
    return PictureOfTheDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )
}
