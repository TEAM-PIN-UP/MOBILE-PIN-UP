package com.pinup.placePinup.di

import com.pinup.placePinup.remote.api.AuthApi
import com.pinup.placePinup.remote.api.BookmarksApi
import com.pinup.placePinup.remote.api.EmailApi
import com.pinup.placePinup.remote.api.ImageApi
import com.pinup.placePinup.remote.api.MembersApi
import com.pinup.placePinup.remote.api.PinBuddyApi
import com.pinup.placePinup.remote.api.PintsApi
import com.pinup.placePinup.remote.api.PlacesApi
import com.pinup.placePinup.remote.api.ReviewsApi
import com.pinup.placePinup.remote.api.createAuthApi
import com.pinup.placePinup.remote.api.createBookmarksApi
import com.pinup.placePinup.remote.api.createEmailApi
import com.pinup.placePinup.remote.api.createImageApi
import com.pinup.placePinup.remote.api.createMembersApi
import com.pinup.placePinup.remote.api.createPinBuddyApi
import com.pinup.placePinup.remote.api.createPintsApi
import com.pinup.placePinup.remote.api.createPlacesApi
import com.pinup.placePinup.remote.api.createReviewsApi
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
    single<PintsApi> { get<Ktorfit>().createPintsApi() }
}
