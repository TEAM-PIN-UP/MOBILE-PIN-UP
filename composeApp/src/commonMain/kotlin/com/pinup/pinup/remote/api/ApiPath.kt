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
        const val SEND_PASSWORD = "$EMAIL/send-password"
    }

    object ImagePath {
        private const val IMAGE = "api/{type}"
        const val SEVERAL_IMAGE = "$IMAGE/images"
        const val ONE_IMAGE = "$IMAGE/image"
    }

    object PinLog {
        const val PINLOG = "api/reviews"
        const val PINLOG_MODIFY = "$PINLOG/{reviewId}"
        const val LIKE = "$PINLOG_MODIFY/likes"
        const val COMMENT = "$PINLOG_MODIFY/comments"
        const val COMMENT_MODIFY = "$COMMENT/{commentId}"
    }
}