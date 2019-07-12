package app.weather.repo

import com.leolei.weather.WeatherRepository
import com.leolei.weather.interfaceadapters.weather.rest.RestService
import com.leolei.weather.model.Forecast

class WeatherRepositoryImpl(
    private val restService: RestService
) : WeatherRepository {
    override suspend fun getForecast(city: String): Forecast {
        val restForecast = restService.getForecast(city)

        with(restForecast.weather) {
            return Forecast(
                date = System.currentTimeMillis(),
                cityName = restForecast.cityName,
                latitude = restForecast.coordinates.latitude,
                longitude = restForecast.coordinates.longitude,
                temperatureCelsius = temperature,
                minTemperatureCelsius = minTemperature,
                maxTemperatureCelsius = maxTemperature,
                pressureHpa = pressure,
                humidity = humidity
            )
        }
    }
}