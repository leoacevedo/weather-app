package com.leolei.cities.history

import com.leolei.weather.model.ModelConstants

interface CityHistoryDao {
     suspend fun insertCity(cityHistoryElement: CityHistoryElement)
     suspend fun removeCity(cityName: String)
     suspend fun clearHistory()
     suspend fun pruneOlderCities(limit: Int = ModelConstants.HISTORY_LIMIT)
     suspend fun getCityHistory(limit: Int): List<CityHistoryElement>
}