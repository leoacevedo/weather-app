package com.leolei.weather.model

data class Forecast(
    val date: Long,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperatureCelsius: Double,
    val minTemperatureCelsius: Double,
    val maxTemperatureCelsius: Double,
    val pressureMmHg: Double,
    val humidity: Double
)