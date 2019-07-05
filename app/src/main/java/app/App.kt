package app

import android.app.Activity
import android.app.Application
import app.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .app(this)
            .baseUrl(getString(R.string.base_url))
            .timeout(resources.getInteger(R.integer.timeout))
            .weatherAppId(getString(R.string.weather_app_id))
            .defaultUnits(getString(R.string.default_units))
            .build()
            .inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }
}
