package com.pinup.pinup.di

import com.pinup.pinup.remote.api.AuthApi
import com.pinup.pinup.remote.api.BookmarksApi
import com.pinup.pinup.remote.api.EmailApi
import com.pinup.pinup.remote.api.ImageApi
import com.pinup.pinup.remote.api.MembersApi
import com.pinup.pinup.remote.api.PinBuddyApi
import com.pinup.pinup.remote.api.PlacesApi
import com.pinup.pinup.remote.api.ReviewsApi
import com.pinup.pinup.remote.api.createAuthApi
import com.pinup.pinup.remote.api.createBookmarksApi
import com.pinup.pinup.remote.api.createEmailApi
import com.pinup.pinup.remote.api.createImageApi
import com.pinup.pinup.remote.api.createMembersApi
import com.pinup.pinup.remote.api.createPinBuddyApi
import com.pinup.pinup.remote.api.createPlacesApi
import com.pinup.pinup.remote.api.createReviewsApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val apiModule = module {
    single<AuthApi> { get<Ktorfit>().createAuthApi() }
    single<EmailApi> { get<Ktorfit>().createEmailApi() }
    single<ImageApi> { get<Ktorfit>().createImageApi() }
    single<BookmarksApi> { get<Ktorfit>().createBookmarksApi() }
    single<MembersApi> { get<Ktorfit>().createMembersApi() }
    single<PinBuddyApi> { get<Ktorfit>().createPinBuddyApi() }
    single<PlacesApi> { get<Ktorfit>().createPlacesApi() }
    single<ReviewsApi> { get<Ktorfit>().createReviewsApi() }

}
