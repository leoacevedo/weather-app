package com.leolei.cities.history

abstract class CityHistoryRepository(
    private val cityHistoryDao: CityHistoryDao
) {
    suspend fun pruneOlderCities() {
        cityHistoryDao.pruneOlderCities()
    }

    suspend fun insertCity(cityName: String) {
        cityHistoryDao.apply {
            removeCity(cityName)
            insertCity(createCityHistoryElement(System.currentTimeMillis(), cityName))
        }
    }

    suspend fun clearHistory() {
        cityHistoryDao.clearHistory()
    }

    suspend fun getCityHistory(limit: Int): List<String> {
        return cityHistoryDao.getCityHistory(limit).map { it.cityName }
    }

    suspend fun removeCityFromHistory(city: String) {
        cityHistoryDao.removeCity(city)
    }

    abstract fun createCityHistoryElement(timestamp: Long, cityName: String): CityHistoryElement
}