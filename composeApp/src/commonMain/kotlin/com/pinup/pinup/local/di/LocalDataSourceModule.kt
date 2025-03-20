package com.pinup.pinup.local.di

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.local.impl.MembersLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsMembersLocalDataSource(
        localDataSource: MembersLocalDataSourceImpl,
    ): MembersLocalDataSource
}