package app.di

import com.leolei.cities.history.CityHistoryRepository
import com.leolei.mvp.WeatherPresenter
import com.leolei.mvp.WeatherPresenterImpl
import com.leolei.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class MainActivityModule {
    @Singleton
    @Provides
    fun provideWeatherPresenter(
        weatherRepo: WeatherRepository,
        cityHistoryRepo: CityHistoryRepository
    ): WeatherPresenter {
        return WeatherPresenterImpl(Dispatchers.IO, weatherRepo, cityHistoryRepo)
    }
}
