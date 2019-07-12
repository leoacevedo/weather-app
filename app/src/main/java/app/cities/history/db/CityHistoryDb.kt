package app.cities.history.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RoomCityHistoryElement::class],
    version = 1
)
abstract class CityHistoryDb : RoomDatabase() {
    abstract fun cityHistoryDao(): RoomCityHistoryDao
}