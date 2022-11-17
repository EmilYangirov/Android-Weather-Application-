package com.emilyangirov.weatherproject.Scripts

import java.util.Date

data class CityData (
     val cityName: String,
     val currentDay: DayData,
     val nextDays: List<TemperatureData>,
)

data class DayData (
    //val dayName: String,
    val currentTemp: TemperatureData,
    val tempByHours: List<TemperatureData>,
)

data class TemperatureData(
    val date: String,
    val averageTemp: String,
    val condition: String,
    val imageUrl : String,
)