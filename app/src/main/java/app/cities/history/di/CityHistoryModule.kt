package app.cities.history.di

import androidx.room.Room
import app.App
import app.cities.history.db.CityHistoryDb
import app.cities.history.db.RoomCityHistoryDao
import app.cities.history.repo.CityHistoryRepositoryImpl
import com.leolei.cities.history.CityHistoryDao
import com.leolei.cities.history.CityHistoryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class CityHistoryModule {
    @Singleton
    @Binds
    abstract fun bindCityHistoryDao(roomDao: RoomCityHistoryDao): CityHistoryDao

    @Module
    companion object {
        @Provides
        @Singleton
        @JvmStatic
        fun provideCityHistoryRepository(dao: CityHistoryDao): CityHistoryRepository {
            return CityHistoryRepositoryImpl(dao)
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideCityHistoryDb(app: App): CityHistoryDb {
            return Room.databaseBuilder(app, CityHistoryDb::class.java, "city_history.db")
                .fallbackToDestructiveMigration()
                .build()
        }

        @Singleton
        @Provides
        @JvmStatic
        fun provideCityHistoryDao(db: CityHistoryDb): RoomCityHistoryDao {
            return db.cityHistoryDao()
        }
    }
}