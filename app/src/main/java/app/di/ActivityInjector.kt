package app.di

import app.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityInjector {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}