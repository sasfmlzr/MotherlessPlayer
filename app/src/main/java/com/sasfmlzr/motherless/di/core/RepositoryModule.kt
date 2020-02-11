package com.sasfmlzr.motherless.di.core

import com.sasfmlzr.motherless.data.repository.LocalMotherlessRepository
import com.sasfmlzr.motherless.data.repository.MotherlessRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    internal abstract fun dvachRepository(repository: LocalMotherlessRepository): MotherlessRepository

}
