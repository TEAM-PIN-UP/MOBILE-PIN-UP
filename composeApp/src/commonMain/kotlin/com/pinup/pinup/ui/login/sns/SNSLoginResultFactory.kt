//package com.pinup.pinup.ui.login.sns
//
//import com.pinup.pinup.ui.login.model.SNSType
//import com.pinup.pinup.ui.login.sns.controller.GoogleLoginController
//import com.pinup.pinup.ui.login.sns.controller.KakaoLoginController
//import com.pinup.pinup.ui.login.sns.controller.NaverLoginController
//import com.pinup.pinup.ui.login.sns.controller.SNSLoginController
//
//object SNSLoginResultFactory {
//    fun initialize(snsType: SNSType): SNSLoginController {
//        return when (snsType) {
//            SNSType.KAKAO -> KakaoLoginController()
//            SNSType.NAVER -> NaverLoginController()
//            SNSType.GOOGLE -> GoogleLoginController()
//        }
//    }
//}