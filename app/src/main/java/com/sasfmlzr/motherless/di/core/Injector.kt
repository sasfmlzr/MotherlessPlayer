package com.sasfmlzr.motherless.di.core


object Injector {

    lateinit var component: FullApplicationComponent

    fun prepare(application: MainApplication) {
        component = DaggerFullApplicationComponent.builder()
                .applicationModule(ApplicationModule(application))
                .build()
    }

    fun applicationComponent() = component
    fun fragmentComponent() = component.fragmentComponent()
}
