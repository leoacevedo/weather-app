package com.leolei.weather.interfaceadapters.weather.rest

import com.google.gson.annotations.SerializedName

data class RestForecast(
    @SerializedName("coord")
    val coordinates: Coordinates,

    @SerializedName("weather")
    val weatherProperties: List<WeatherProperty>,

    @SerializedName("main")
    val weather: Weather,

    val timezone: Int,

    @SerializedName("name")
    val cityName: String,

    @SerializedName("sys")
    val misc: Misc,

    @SerializedName("code")
    val httpCode: Int
)

data class Coordinates(
    @SerializedName("lon")
    val longitude: Double,

    @SerializedName("lat")
    val latitude: Double
)

data class WeatherProperty(
    val id: Int,

    @SerializedName("main")
    val name: String,

    val description: String,
    val icon: String
)

data class Weather(
    @SerializedName("temp")
    val temperature: Double,

    @SerializedName("temp_max")
    val maxTemperature: Double,

    @SerializedName("temp_min")
    val minTemperature: Double,
    val pressure: Double,

    @SerializedName("humidity")
    val humidity: Double
)

data class Misc(
    val sunrise: Int,
    val sunset: Int,
    val country: String
)
