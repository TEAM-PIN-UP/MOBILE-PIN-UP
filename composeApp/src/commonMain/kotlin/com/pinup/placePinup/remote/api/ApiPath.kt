package com.pinup.placePinup.remote.api

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
        const val FIND_ID_BY_NICKNAME = "$MEMBERS/find-id-by-nickname"
        const val FIND_ID_BY_EMAIL = "$MEMBERS/find-id-by-email"
        const val CHANGE_PASSWORD = "$MEMBERS/password"
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

    object Pints {
        const val PINTS = "api/pints"
        const val PINTS_MODIFY = "$PINTS/{pintsId}"
        const val EDITOR = "api/editor"
        const val EDITOR_CATEGORY = "$EDITOR/category"
        const val EDITOR_PINCH = "$EDITOR/{pintsId}"
    }

    object Report {
        const val REPORT = "api/reports"
        const val BLOCK_USER = "$REPORT/userblock"
        const val UNDO_BLOCK_USER = "$BLOCK_USER/{blockedUserId}"
        const val REPORT_USER = "$REPORT/user"
        const val REPORT_PINLOG = "$REPORT/pints"
        const val REPORT_COMMENT = "$REPORT/comment"
    }

    object Call {
        const val CALL = "api/call"
        const val DEVICE_TOKEN = "$CALL/device-tokens"
        const val NOTIFICATION = "$CALL/notifications"
        const val READ_ALL_NOTIFICATION = "$NOTIFICATION/read-all"
        const val READ_NOTIFICATION = "$NOTIFICATION/{notificationId}/read"
        const val DELETE_NOTIFICATION = "$NOTIFICATION/{notificationId}"
    }
}