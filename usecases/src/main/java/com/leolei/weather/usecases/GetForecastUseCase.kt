package com.leolei.weather.usecases

import com.leolei.weather.model.Forecast
import kotlinx.coroutines.Deferred

interface GetForecastUseCase {
    fun getForecastAsync(city: String): Deferred<Forecast>
}
