package com.pinup.pinup.data.di

import com.pinup.pinup.data.impl.AuthRepositoryImpl
import com.pinup.pinup.data.impl.BookmarksRepositoryImpl
import com.pinup.pinup.data.impl.MembersRepositoryImpl
import com.pinup.pinup.data.impl.PinBuddyRepositoryImpl
import com.pinup.pinup.data.impl.PlacesRepositoryImpl
import com.pinup.pinup.data.impl.ReviewsRepositoryImpl
import com.pinup.pinup.domain.repository.AuthRepository
import com.pinup.pinup.domain.repository.BookmarksRepository
import com.pinup.pinup.domain.repository.MembersRepository
import com.pinup.pinup.domain.repository.PinBuddyRepository
import com.pinup.pinup.domain.repository.PlacesRepository
import com.pinup.pinup.domain.repository.ReviewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        repository: AuthRepositoryImpl,
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindsMembersRepository(
        repository: MembersRepositoryImpl,
    ): MembersRepository

    @Binds
    @Singleton
    abstract fun bindsPlacesRepository(
        repository: PlacesRepositoryImpl
    ): PlacesRepository

    @Binds
    @Singleton
    abstract fun bindsReviewsRepository(
        repository: ReviewsRepositoryImpl
    ): ReviewsRepository

    @Binds
    @Singleton
    abstract fun bindsBookmarksRepository(
        repository: BookmarksRepositoryImpl
    ): BookmarksRepository

    @Binds
    abstract fun bindsPinBuddyRepository(
        repository: PinBuddyRepositoryImpl
    ): PinBuddyRepository
}