package com.leolei.weather.interfaceadapters.weather

import com.leolei.weather.interfaceadapters.weather.rest.RestService
import com.leolei.weather.model.Forecast
import com.leolei.weather.usecases.GetForecastUseCase

class WeatherRepository(
    private val restService: RestService
) : GetForecastUseCase {
    override suspend fun getForecast(city: String): Forecast {
        val restForecast = restService.getForecast(city)

        with(restForecast.weather) {
            return Forecast(
                date = System.currentTimeMillis(),
                cityName = restForecast.cityName,
                temperatureCelsius = temperature,
                minTemperatureCelsius = minTemperature,
                maxTemperatureCelsius = maxTemperature,
                pressureMmHg = pressure,
                humidity = humidity
            )
        }
    }
}