package com.leolei.mvp

interface WeatherPresenter {
    fun onShowing(view: WeatherView)
    fun onHiding()

    fun getForecast(city: String)
}