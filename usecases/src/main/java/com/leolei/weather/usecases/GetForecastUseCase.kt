package com.leolei.weather.usecases

import com.leolei.weather.model.Forecast

interface GetForecastUseCase {
    suspend fun getForecast(city: String): Forecast
}
