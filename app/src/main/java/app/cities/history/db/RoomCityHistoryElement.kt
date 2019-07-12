package app.cities.history.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.leolei.cities.history.CityHistoryElement

@Entity(tableName = "history", indices = [Index("timestamp")])
data class RoomCityHistoryElement(
    @ColumnInfo(name = "timestamp")
    override val timestamp: Long,

    @PrimaryKey
    @ColumnInfo(name = "cityName")
    override val cityName: String
): CityHistoryElement()