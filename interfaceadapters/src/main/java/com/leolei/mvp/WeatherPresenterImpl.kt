package com.leolei.mvp

import com.leolei.cities.history.CityHistoryRepository
import com.leolei.weather.WeatherRepository
import com.leolei.weather.model.ModelConstants
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeatherPresenterImpl(
    private val dispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository,
    private val cityHistoryRepository: CityHistoryRepository
) : WeatherPresenter {

    private lateinit var job: Job
    private lateinit var coroutineScope: CoroutineScope

    private var view: WeatherView? = null

    override fun onShowing(view: WeatherView) {
        this.view = view
        job = Job()
        coroutineScope = CoroutineScope(dispatcher + job)

        updateCitySuggestions()
    }

    override fun onHiding() {
        this.view = null
        job.cancel()
    }

    override fun getForecast(city: String) {
        coroutineScope.launch {
            view?.showLoading()

            try {
                cityHistoryRepository.insertCity(city)
                cityHistoryRepository.pruneOlderCities()
                val forecast = weatherRepository.getForecast(city)
                view?.render(forecast)

                updateCitySuggestions()
            } catch (e: Exception) {
                view?.render(e)
                e.printStackTrace()
            }

            view?.hideLoading()
        }
    }

    override fun removeCityFromHistory(city: String) {
        coroutineScope.launch {
            try {
                cityHistoryRepository.removeCityFromHistory(city)
                updateCitySuggestions()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun updateCitySuggestions() {
        coroutineScope.launch {
            try {
                val cities = cityHistoryRepository.getCityHistory(ModelConstants.HISTORY_LIMIT)
                view?.updateCitySuggestions(cities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}