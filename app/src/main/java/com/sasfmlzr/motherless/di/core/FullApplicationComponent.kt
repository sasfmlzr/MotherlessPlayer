package com.sasfmlzr.motherless.di.core

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class, ThirdModule::class, RepositoryModule::class
])
interface FullApplicationComponent {
    fun inject(mainApplication: MainApplication)
    fun fragmentComponent(): FragmentComponent
}
