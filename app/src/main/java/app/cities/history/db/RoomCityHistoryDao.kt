package app.cities.history.db

import android.app.SearchManager.SUGGEST_COLUMN_TEXT_1
import android.database.Cursor
import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.leolei.cities.history.CityHistoryDao
import com.leolei.cities.history.CityHistoryElement

@Dao
abstract class RoomCityHistoryDao : CityHistoryDao {
    override suspend fun insertCity(cityHistoryElement: CityHistoryElement) {
        insertCity(cityHistoryElement as RoomCityHistoryElement)
    }

    override suspend fun pruneOlderCities(limit: Int) {
        val recentCities = getCityHistory(limit)
        if (recentCities.size == limit) {
            val oldTimestamp = recentCities.last().timestamp
            pruneHistory(oldTimestamp)
        }
    }

    @Insert
    abstract suspend fun insertCity(cityHistoryElement: RoomCityHistoryElement)

    @Query("DELETE from history WHERE cityName=:cityName")
    abstract override suspend fun removeCity(cityName: String)

    @Query("DELETE from history")
    abstract override suspend fun clearHistory()

    @Query("DELETE FROM history WHERE timestamp < :timestamp")
    abstract suspend fun pruneHistory(timestamp: Long)

    @Query("SELECT * FROM history ORDER BY timestamp DESC LIMIT :limit")
    abstract suspend fun _getCityHistory(limit: Int): List<RoomCityHistoryElement>

    override suspend fun getCityHistory(limit: Int): List<CityHistoryElement> {
        return _getCityHistory(limit)
    }

    @Query(
        "SELECT timestamp AS _id, " +
                "cityName as $SUGGEST_COLUMN_TEXT_1 " +
                "FROM history ORDER BY timestamp DESC LIMIT :limit"
    )
    abstract fun getCityHistoryCursor(limit: Int): Cursor
}