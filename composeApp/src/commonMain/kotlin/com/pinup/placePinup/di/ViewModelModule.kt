package com.pinup.placePinup.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.pinup.placePinup.ui.userprofile.UserProfileViewModel
import com.pinup.placePinup.ui.login.LoginViewModel
import com.pinup.placePinup.ui.signup.SignUpViewModel
import com.pinup.placePinup.ui.setting.SettingViewModel
import com.pinup.placePinup.ui.reviewwrite.WriteReviewViewModel
import com.pinup.placePinup.ui.reviewwrite.successWriteReview.WriteReviewDetailViewModel
import com.pinup.placePinup.ui.pinbuddy.PinBuddyViewModel
import com.pinup.placePinup.ui.my.MyViewModel
import com.pinup.placePinup.ui.map.MapViewModel
import com.pinup.placePinup.ui.main.MainViewModel
import com.pinup.placePinup.ui.bookmark.BookmarkViewModel
import com.pinup.placePinup.ui.addpinbuddy.AddPinBuddyViewModel
import com.pinup.placePinup.ui.reviewwrite.searchplace.SearchPlaceViewModel
import com.pinup.placePinup.StartAppViewModel
import com.pinup.placePinup.ui.article.ArticleViewModel
import com.pinup.placePinup.ui.article.detail.ArticleDetailViewModel
import com.pinup.placePinup.ui.feed.FeedViewModel
import com.pinup.placePinup.ui.findAccount.changePassword.ChangePasswordViewModel
import com.pinup.placePinup.ui.findAccount.findId.FindIdViewModel
import com.pinup.placePinup.ui.findAccount.findPassword.FindPasswordViewModel
import com.pinup.placePinup.ui.my.pinch.PinchWriteViewModel
import com.pinup.placePinup.ui.my.pinch.detail.PinchDetailViewModel
import com.pinup.placePinup.ui.my.scrap.ScrapViewModel
import com.pinup.placePinup.ui.onboarding.OnboardingViewModel
import com.pinup.placePinup.ui.onboarding.choiceSignup.ChoiceSignUpViewModel
import com.pinup.placePinup.ui.pinlogDetail.PinlogDetailViewModel
import com.pinup.placePinup.ui.placeDetail.PlaceDetailViewModel
import com.pinup.placePinup.ui.profilesetting.ProfileSettingViewModel
import com.pinup.placePinup.ui.setting.unregister.UnRegisterViewModel

val viewModelModule = module {
    viewModelOf(::OnboardingViewModel)
    viewModelOf(::ChoiceSignUpViewModel)
    viewModelOf(::UserProfileViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::FindPasswordViewModel)
    viewModelOf(::FindIdViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::SettingViewModel)
    viewModelOf(::UnRegisterViewModel)
    viewModelOf(::ProfileSettingViewModel)
    viewModelOf(::WriteReviewViewModel)
    viewModelOf(::PinlogDetailViewModel)
    viewModelOf(::WriteReviewDetailViewModel)
    viewModelOf(::SearchPlaceViewModel)
    viewModelOf(::PinBuddyViewModel)
    viewModelOf(::MyViewModel)
    viewModelOf(::MapViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::BookmarkViewModel)
    viewModelOf(::FeedViewModel)
    viewModelOf(::ArticleViewModel)
    viewModelOf(::ArticleDetailViewModel)
    viewModelOf(::AddPinBuddyViewModel)
    viewModelOf(::StartAppViewModel)
    viewModelOf(::ScrapViewModel)
    viewModelOf(::PlaceDetailViewModel)
    viewModelOf(::PinchWriteViewModel)
    viewModelOf(::PinchDetailViewModel)
}