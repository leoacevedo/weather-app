package com.leolei.weather

import com.leolei.weather.model.Forecast
import com.leolei.weather.usecases.GetForecastUseCase

interface WeatherRepository : GetForecastUseCase {
    override suspend fun getForecast(city: String): Forecast
}