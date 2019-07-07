package app.di

import com.leolei.mvp.WeatherPresenter
import com.leolei.mvp.WeatherPresenterImpl
import com.leolei.weather.interfaceadapters.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
class MainActivityModule {
    @Singleton
    @Provides
    fun provideWeatherPresenter(repo: WeatherRepository): WeatherPresenter {
        return WeatherPresenterImpl(Dispatchers.IO, repo)
    }
}
