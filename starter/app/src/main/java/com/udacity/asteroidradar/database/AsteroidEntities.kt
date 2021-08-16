package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_asteroid_info_table")
data class AsteroidEntities(
    @PrimaryKey
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
)