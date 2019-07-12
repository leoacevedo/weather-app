package app.cities.history.ui

import android.os.Parcelable
import com.leolei.weather.model.Forecast
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ParcelizableForecast(
    val date: Long,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperatureCelsius: Double,
    val minTemperatureCelsius: Double,
    val maxTemperatureCelsius: Double,
    val pressureHpa: Double,
    val humidity: Double
) : Parcelable {
    constructor(forecast: Forecast): this(
        forecast.date,
        forecast.cityName,
        forecast.latitude,
        forecast.longitude,
        forecast.temperatureCelsius,
        forecast.minTemperatureCelsius,
        forecast.maxTemperatureCelsius,
        forecast.pressureHpa,
        forecast.humidity
    )
}