package app.cities.history.repo

import app.cities.history.db.RoomCityHistoryElement
import com.leolei.cities.history.CityHistoryDao
import com.leolei.cities.history.CityHistoryElement
import com.leolei.cities.history.CityHistoryRepository

class CityHistoryRepositoryImpl(
    cityHistoryDao: CityHistoryDao
) : CityHistoryRepository(cityHistoryDao) {
    override fun createCityHistoryElement(timestamp: Long, cityName: String): CityHistoryElement {
        return RoomCityHistoryElement(timestamp, cityName)
    }
}