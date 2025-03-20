package com.pinup.pinup.remote.di

import com.pinup.pinup.data.local.MembersLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Singleton
    @Provides
    fun providesAddHeaderInterceptor(
        membersLocalDataSource: MembersLocalDataSource
    ): AddHeaderInterceptor {
        return AddHeaderInterceptor(membersLocalDataSource)
    }

    @Singleton
    @Provides
    fun providesTokenAuthenticator(
        membersLocalDataSource: MembersLocalDataSource
    ): TokenAuthenticator {
        return TokenAuthenticator(membersLocalDataSource)
    }
}