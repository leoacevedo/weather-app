package app.di

import app.App
import com.leolei.weather.interfaceadapters.weather.di.AppId
import com.leolei.weather.interfaceadapters.weather.di.BaseUrl
import com.leolei.weather.interfaceadapters.weather.di.Units
import com.leolei.weather.interfaceadapters.weather.di.WeatherModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    WeatherModule::class,
    ActivityInjector::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent: AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun app(app: App): Builder

        @BindsInstance
        fun baseUrl(@BaseUrl baseUrl: String): Builder

        @BindsInstance
        fun weatherAppId(@AppId appId: String): Builder

        @BindsInstance
        fun defaultUnits(@Units units: String): Builder

        @BindsInstance
        fun timeout(timeout: Int): Builder
    }
}