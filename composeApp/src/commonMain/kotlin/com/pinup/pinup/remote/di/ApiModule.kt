package com.pinup.pinup.remote.di

import com.pinup.pinup.remote.api.AuthApi
import com.pinup.pinup.remote.api.BookmarksApi
import com.pinup.pinup.remote.api.MembersApi
import com.pinup.pinup.remote.api.PinBuddyApi
import com.pinup.pinup.remote.api.PlacesApi
import com.pinup.pinup.remote.api.ReviewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)

    @Singleton
    @Provides
    fun provideMembersApi(retrofit: Retrofit): MembersApi =
        retrofit.create(MembersApi::class.java)

    @Singleton
    @Provides
    fun providePlacesApi(retrofit: Retrofit): PlacesApi =
        retrofit.create(PlacesApi::class.java)

    @Singleton
    @Provides
    fun provideReviewsApi(retrofit: Retrofit): ReviewsApi =
        retrofit.create(ReviewsApi::class.java)

    @Singleton
    @Provides
    fun provideBookmarksApi(retrofit: Retrofit): BookmarksApi =
        retrofit.create(BookmarksApi::class.java)

    @Singleton
    @Provides
    fun providePinBuddyApi(retrofit: Retrofit): PinBuddyApi =
        retrofit.create(PinBuddyApi::class.java)
}