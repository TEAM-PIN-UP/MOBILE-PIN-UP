package com.pinup.pinup.remote.di

import com.pinup.pinup.data.remote.AuthRemoteDataSource
import com.pinup.pinup.data.remote.BookmarksRemoteDataSource
import com.pinup.pinup.data.remote.MembersRemoteDataSource
import com.pinup.pinup.data.remote.PinBuddyRemoteDataSource
import com.pinup.pinup.data.remote.PlacesRemoteDataSource
import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.remote.impl.AuthRemoteDataSourceImpl
import com.pinup.pinup.remote.impl.BookmarksRemoteDataSourceImpl
import com.pinup.pinup.remote.impl.MembersRemoteDataSourceImpl
import com.pinup.pinup.remote.impl.PinBuddyRemoteDataSourceImpl
import com.pinup.pinup.remote.impl.PlacesRemoteDataSourceImpl
import com.pinup.pinup.remote.impl.ReviewsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRemoteDataSource(
        remoteDataSource: AuthRemoteDataSourceImpl,
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsMembersRemoteDataSource(
        remoteDataSource: MembersRemoteDataSourceImpl,
    ): MembersRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsPlacesRemoteDataSource(
        remoteDataSource: PlacesRemoteDataSourceImpl
    ): PlacesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsReviewsRemoteDataSource(
        remoteDataSource: ReviewsRemoteDataSourceImpl
    ): ReviewsRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindsBookmarksRemoteDataSource(
        remoteDataSource: BookmarksRemoteDataSourceImpl
    ): BookmarksRemoteDataSource

    @Binds
    abstract fun bindsPinBuddyRemoteDataSource(
        remoteDataSource: PinBuddyRemoteDataSourceImpl
    ): PinBuddyRemoteDataSource
}