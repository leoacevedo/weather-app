package com.leolei.mvp

import com.leolei.weather.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeatherPresenterImpl(
    private val dispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository
) : WeatherPresenter {

    private lateinit var job: Job
    private lateinit var coroutineScope: CoroutineScope

    private var view: WeatherView? = null

    override fun onShowing(view: WeatherView) {
        this.view = view
        job = Job()
        coroutineScope = CoroutineScope(dispatcher + job)
    }

    override fun onHiding() {
        this.view = null
        job.cancel()
    }

    override fun getForecast(city: String) {
        coroutineScope.launch {
            view?.showLoading()

            try {
                val forecast = weatherRepository.getForecast(city)
                view?.render(forecast)
            } catch (e: Exception) {
                view?.render(e)
                e.printStackTrace()
            }

            view?.hideLoading()
        }
    }
}