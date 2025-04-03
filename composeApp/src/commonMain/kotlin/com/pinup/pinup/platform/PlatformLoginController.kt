package com.pinup.pinup.platform

import com.pinup.pinup.ui.login.sns.SNSLoginController

expect class GoogleLoginController() : SNSLoginController
expect class KakaoLoginController() : SNSLoginController
expect class NaverLoginController() : SNSLoginController
