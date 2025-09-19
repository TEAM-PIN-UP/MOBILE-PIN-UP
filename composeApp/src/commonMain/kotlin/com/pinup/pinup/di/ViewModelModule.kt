package com.pinup.pinup.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.pinup.pinup.ui.userprofile.UserProfileViewModel
import com.pinup.pinup.ui.login.LoginViewModel
import com.pinup.pinup.ui.signup.SignUpViewModel
import com.pinup.pinup.ui.setting.SettingViewModel
import com.pinup.pinup.ui.reviewwrite.WriteReviewViewModel
import com.pinup.pinup.ui.reviewwrite.successWriteReview.WriteReviewDetailViewModel
import com.pinup.pinup.ui.pinbuddy.PinBuddyViewModel
import com.pinup.pinup.ui.my.MyViewModel
import com.pinup.pinup.ui.map.MapViewModel
import com.pinup.pinup.ui.main.MainViewModel
import com.pinup.pinup.ui.bookmark.BookmarkViewModel
import com.pinup.pinup.ui.addpinbuddy.AddPinBuddyViewModel
import com.pinup.pinup.ui.reviewwrite.searchplace.SearchPlaceViewModel
import com.pinup.pinup.StartAppViewModel
import com.pinup.pinup.ui.article.ArticleViewModel
import com.pinup.pinup.ui.article.detail.ArticleDetailViewModel
import com.pinup.pinup.ui.feed.FeedViewModel
import com.pinup.pinup.ui.findAccount.changePassword.ChangePasswordViewModel
import com.pinup.pinup.ui.findAccount.findId.FindIdViewModel
import com.pinup.pinup.ui.findAccount.findPassword.FindPasswordViewModel
import com.pinup.pinup.ui.onboarding.OnboardingViewModel
import com.pinup.pinup.ui.onboarding.choiceSignup.ChoiceSignUpViewModel
import com.pinup.pinup.ui.pinlogDetail.PinlogDetailViewModel
import com.pinup.pinup.ui.profilesetting.ProfileSettingViewModel

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
}