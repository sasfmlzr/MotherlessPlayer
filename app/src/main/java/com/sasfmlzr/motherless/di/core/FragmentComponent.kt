package com.sasfmlzr.motherless.di.core

import com.sasfmlzr.motherless.di.base.FragmentScope
import com.sasfmlzr.motherless.ui.home.HomeFragment
import com.sasfmlzr.motherless.ui.player.PlayerFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [FragmentVMFactoryModule::class])
interface FragmentComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: PlayerFragment)
}
