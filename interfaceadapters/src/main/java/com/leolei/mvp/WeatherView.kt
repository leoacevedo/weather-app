package com.leolei.mvp

import com.leolei.weather.model.Forecast

interface WeatherView {
    fun showLoading()
    fun hideLoading()

    fun render(forecast: Forecast)
    fun render(error: Throwable)
}