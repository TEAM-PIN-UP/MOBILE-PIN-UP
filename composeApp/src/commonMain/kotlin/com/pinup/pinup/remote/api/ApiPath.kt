package com.pinup.pinup.remote.api

import com.pinup.pinup.remote.api.ApiPath.Auth.AUTH

object ApiPath {
    object Auth{
        private const val AUTH = "api/auth"
        const val REFRESH = "$AUTH/refresh"
        const val SOCIAL_LOGIN = "$AUTH/social-login"
        const val EMAIL_LOGIN = "$AUTH/login"
        const val LOGOUT = "$AUTH/logout"
    }

    object Members {
        const val MEMBERS = "api/members"
        const val SOCIAL_SIGN_UP = "$MEMBERS/social-sign-up"
        const val EMAIL_SIGN_UP = "$MEMBERS/sign-up"
    }

    object MailPath {
        private const val EMAIL = "api/mail"
        const val VERIFY_CODE = "$EMAIL/verify-code"
        const val SEND_CODE = "$EMAIL/send-code"
    }
}