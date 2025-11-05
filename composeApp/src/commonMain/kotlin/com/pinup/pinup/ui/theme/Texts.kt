package com.pinup.pinup.ui.theme


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

object Texts {
    fun buildHighlightedText(
        text: String,
        keyword: String?,
        color: Color,
        ignoreCase: Boolean = true
    ): AnnotatedString {
        if (keyword.isNullOrBlank()) return AnnotatedString(text)

        val builder = AnnotatedString.Builder(text)
        val pattern = Regex(Regex.escape(keyword), if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet())

        pattern.findAll(text).forEach { match ->
            builder.addStyle(
                SpanStyle(
                    color = color,
                ),
                start = match.range.first,
                end = match.range.last + 1
            )
        }
        return builder.toAnnotatedString()
    }

    object Word {
        const val WORD_START = "시작하기"
        const val WORD_ID = "아이디"
        const val WORD_PASSWORD = "비밀번호"
        const val WORD_LOGIN = "로그인"
        const val NEXT = "다음"
        const val SKIP = "건너뛰기"
        const val DO_GO = "계속하기"
        const val EMAIL = "이메일"
        const val CONFIRM = "확인"
        const val DO_CONFiRM = "확인하기"
        const val VERIFY = "인증하기"
        const val PIN_MAP = "핀맵"
        const val PINLOG = "핀로그"
        const val FEED = "피드"
        const val ARTICLE = "아티클"
        const val MY = "마이"
        const val RATING = "별점"
        const val DO_RETURN = "돌아가기"
        const val DELETE = "삭제"
        const val DO_DELETE = "삭제하기"
        const val COMMENT = "댓글"
        const val DO_REPLY_COMMENT = "답글달기"
        const val DO_REGISTER = "등록하기"
        const val SEE_MORE = "자세히 보기"
        const val PIN_BUDDY = "핀버디"
        const val PINCH = "핀츠"
        const val SCRAP = "스크랩"
        const val ACCEPT = "수락"
        const val REFUSE = "거절"
        const val CLOSE = "닫기"
        const val SETTING = "설정"
        const val ETC = "기타"
        const val COMPLETE = "완료"
        const val NICKNAME = "닉네임"
        const val INTRO = "소개"
        const val UNREGISTER = "탈퇴하기"
        const val SIGN_UP = "회원가입"
    }

    object Onboarding {
        val ONBOARDING_TEXT = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray50)) {
                append("우리의 로컬 아카이브, ")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("핀업")
            }
        }
        val ONBOARDING_LOGIN_TITLE = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("우리의 로컬 아카이브, ")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("핀업")
            }
        }
        const val ONBOARDING_LOGIN_TEXT = "로그인하고 나만의 공간을 공유해보세요!"
    }

    object Login {
        const val LOGIN_ID_FIND = "아이디 찾기"
        const val LOGIN_SNS = "SNS 로그인"
        const val INCORRECT_ID = "아이디 혹은 비밀번호가 일치하지 않습니다."
    }

    object FindPassword {
        const val PASSWORD_HINT = "비밀번호는 8~20자, 특수문자 포함 필수입니다."
        const val CHANGE_PASSWORD = "비밀번호 변경하기"
        const val CHANGE_PASSWORD_HINT = "임시 비밀번호로 로그인 시, 비밀번호 변경이 필요합니다."
        const val SEND_PASSWORD = "비밀번호 발송"
        const val PASSWORD_AGAIN_HINT = "비밀번호 확인"
        const val FIND_PASSWORD = "비밀번호 찾기"
        const val FIND_PASSWORD_HINT = "입력한 주소로 임시 비밀번호가 전송됩니다."
        const val SENT_PASSWORD_TITLE = "이메일이 전송되었습니다!"
        const val SENT_PASSWORD_HINT = "이메일로 전송 받은 임시 비밀번호로 로그인 해주세요."
        const val DO_LOGIN = "로그인하기"
    }

    object FindId {
        const val FIND_ID = "아이디 찾기"
        const val INPUT_NICKNAME = "닉네임 입력"
        const val FIND_ID_BY_EMAIL = "이메일 주소로 찾기"
        const val FIND_ID_BY_NICKNAME = "닉네임으로 찾기"
        const val FIND_ID_RESULT = "해당 이메일로 가입된 계정이 있습니다.\n비밀번호를 변경하고 싶으신가요?"
        const val NOT_EXIST_NICKNAME = "존재하지 않는 닉네임입니다."
        fun getNickNameTitle(nickName : String) = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("안녕하세요\n")
            }
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append(nickName)
            }
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("님!")
            }
        }
    }

    object SignupEmail {
        const val TITLE = "로그인에 사용할\n이메일을 입력해주세요."
        const val HINT = "이메일 주소를 입력해주세요."
        const val INVALID = "유효한 이메일 주소가 아닙니다."
        const val DUPLICATE = "동일한 이메일 주소로 가입된 계정이 있습니다.\n기존 계정을 확인해주세요."
        const val CODE_HINT = "인증번호를 입력해주세요."
        const val CODE_INVALID = "인증번호가 일치하지 않습니다."
        const val CODE_VALID = "인증번호가 일치합니다."
        const val SEND_CODE = "인증번호가 전송되었습니다."
    }

    object SignupPassword {
        const val TITLE = "사용하실 비밀번호를\n입력해주세요."
        const val HINT = "비밀번호 입력 (8자이상 + 특수문자 포함)"
        const val INVALID = "비밀번호는 8~20자, 특수문자 포함 필수입니다."
        const val CONFIRM = "비밀번호 확인"
        const val NOT_MATCH = "입력된 비밀번호가 일치하지 않습니다."
    }

    object SignupTerms {
        const val TITLE = "서비스 이용약관에\n동의해주세요."
        const val ALL_AGREE = "약관 전체 동의"
        const val SERVICE_AGREE = "이용약관 동의 (필수)"
        const val PRIVATE_INFO_AGREE = "개인정보 수집 및 이용 동의 (필수)"
        const val LOCATION_INFO_AGREE = "위치정보 이용 동의 (필수)"
        const val MARKETING_AGREE = "홍보 및 마케팅 이용 동의 (선택)"
    }

    object SignupNickname {
        const val NICKNAME_TITLE = "반가워요!\n닉네임을 만들어볼까요?"
        const val INPUT_NICKNAME = "닉네임 입력"
        const val NICKNAME_HINT = "닉네임은 나중에 언제든지 변경가능해요."
        const val CONDITION_NICKNAME = "한글, 영문만 입력 가능"
        const val DUPLICATE_NICKNAME = "중복되는 닉네임이에요."
    }

    object SignupProfile {
        const val PROFILE_TITLE = "프로필에 사용 될\n이미지를 골라주세요."
        const val PROFILE_REGISTER = "멋진 프로필\n사진이 등록되었어요!"
        const val PROFILE_SELECT = "사진 선택하기"
    }

    object PinMap {
        const val SEARCH_BUTTON = "현 지도에서 검색"
        const val SEARCH_HINT = "장소/위치 검색하기"
        const val EMPTY_PINLOG = "아직 핀로그가 없어요!\n나만의 장소를 핀업해보세요:)"
        const val SEARCH_RESULT = "검색 결과"
        const val GO_ARTICLE = "아티클 바로가기"
    }

    object PinLog {
        const val WRITE_PINLOG = "핀로그 작성"
        const val WRITE_PINLOG_TITLE = "핀로그 작성하기"
        const val REGISTER_PINLOG = "핀로그 등록하기"
        const val WRITE_TITLE = "어떤 장소의 핀로그를\n작성할까요?"
        const val WRITE_DESCRIPTION = "작성된 핀로그는 핀버디만 볼 수 있어요"
        const val SEARCH_HINT = "장소 검색하기"
        const val SEARCH_RESULT = "검색 결과"
        fun getSelectDateTitle(place : String) = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Colors.Main)) {
                append("‘${place}’")
            }
            withStyle(style = SpanStyle(color = Colors.Gray800)) {
                append("은\n 언제 방문하셨나요?")
            }
        }
        const val SELECT_DATE_HINT = "날짜 선택"
        const val IMAGE_UPLOAD = "사진 업로드"
        const val PINLOG_HINT = "작성된 핀로그는 나의 핀버디들에게만 보여요!\n\n" +
                "*주의: 욕설, 비방 목적 혹은 명예 훼손성 내용은 작성 시 삭제 처리 될 수 있습니다."
        const val PINLOG_MORE_LENGTH = "10자 이상 작성해 주세요."
        const val PINLOG_TOO_MUCH_LENGTH = "입력 가능 글자 수를 초과했어요."
        const val PINLOG_DIALOG_TITLE = "핀로그 작성 완료!"
        const val PINLOG_DIALOG_BODY = "등록한 핀로그를\n확인하시겠어요?"
        const val DETAIL_TITLE = "핀로그 상세"
        const val COMMENT_HINT = "댓글을 입력하세요"
        const val COMMENT_REPLY_HINT = "답글을 입력하세요"
        const val EDIT_PINLOG = "핀로그 수정"
        const val DELETE_PINLOG = "핀로그 삭제"
        const val EDIT_COMMENT = "댓글 수정"
        const val DELETE_COMMENT = "댓글 삭제"
        const val DELETE_DIALOG_TITLE = "핀로그를 삭제할까요?"
        const val DELETE_DIALOG_DESCRIPTION = "삭제된 핀로그는 복구할 수 없습니다."
        const val DELETE_COMMENT_DIALOG_TITLE = "댓글을 삭제할까요?"
        const val DELETE_COMMENT_DIALOG_DESCRIPTION = "삭제된 댓글은 복구할 수 없습니다."

    }

    object FEED {
        const val RECENT_SEARCH = "최근 검색어"
        const val EMPTY_FEED = "아직 핀버디들의 핀로그가 없어요!"
    }

    object PROFILE {
        const val PROFILE_MY = "MY"
        const val AVERAGE_STAR_RATING = "평균 별점"
        const val SHARE_PROFILE = "프로필 공유"
        const val ADD_PIN_BUDDY = "핀버디 추가"
        const val RECEIVE_REQUEST = "받은 신청"
        const val SENT_REQUEST = "보낸 신청"
        const val CANCEL_SENT_REQUEST = "신청 취소"
        const val DIALOG_DELETE_PIN_BUDDY_TITLE = "핀버디를 삭제할까요?"
        const val DIALOG_DELETE_PIN_BUDDY_DESCRIPTION = "삭제한 핀버디에게 언제든지\n다시 핀버디를 신청할 수 있습니다."
        const val EMPTY_PIN_BUDDY = "아직 핀버디가 없어요!\n먼저 핀버디를 신청해 보는 건 어때요?"
        const val EMPTY_RECEIVE_PIN_BUDDY = "받은 핀버디 신청이 없어요.\n핀버디 신청이 오면 이곳에 표시돼요!"
        const val EMPTY_SENT_PIN_BUDDY = "보낸 신청이 없어요.\n나와 취향이 비슷한 핀버디를 찾아보세요!"
        const val EMPTY_MY_PINLOG = "아직 핀로그가 없어요!\n나만의 장소를 핀업해보세요:)"
        const val EMPTY_USER_PINLOG = "핀버디가 작성한 핀로그가 없어요!\n조금만 더 기다려볼까요?"
        const val SEARCH_RESULT = "검색 결과"
        const val ALREADY_REQUEST_PIN_BUDDY = "요청 진행중"
        const val REQUEST_PIN_BUDDY = "핀버디 신청"
        const val CANCEL_PIN_BUDDY_REQUEST = "핀버디 신청 취소하기"
        const val REMOVE_PIN_BUDDY_DIALOG_TITLE = "핀버디를 삭제할까요?"
        const val REMOVE_PIN_BUDDY_DIALOG_DESCRIPTION = "삭제한 핀버디에게 언제든지\n다시 핀버디를 신청할 수 있습니다."
        const val ROCK_PROFILE = "작성된 핀로그가 궁금하다면\n핀버디를 신청해 주세요!"
        const val WRITE_FIRST_PINLOG = "첫 핀로그 작성하기"
        const val EMPTY_SCRAP = "저장된 장소가 없어요!\n핀로그를 살펴보며 장소를 살펴보아요."
        const val GO_PINLOG = "핀로그 보러가기"
        fun EMPTY_PINCH(name: String) = "${name}님 만의 코스로 핀츠를 만들어보아요."
        const val GO_PINCH_CREATE = "핀츠 만들러 가기"
    }

    object Article {
        const val TITLE = "핀업 아티클"
        const val RECOMMEND_ARTICLE = "핀업 에디터가 찾아낸\n또 다른 코스를 만나보세요."
    }

    object Setting {
        const val PROFILE_SETTING_TITLE = "계정 설정"
        const val PROFILE_SETTING = "프로필 편집"
        const val PROFILE_INFO = "계정 정보"
        const val CHANGE_PASSWORD = "비밀번호 변경"
        const val CS_TITLE = "고객센터"
        const val QA = "문의하기"
        const val SUGGEST = "건의하기"
        const val SERVICE_TERM = "이용약관"
        const val PERSONAL_TERM = "개인정보처리방침"
        const val VERSION = "앱버전"
        const val LOGOUT = "로그아웃"
        const val UNREGISTER = "회원탈퇴"
        const val HINT_BIO_CHANGE = "60자 이내로 나를 소개해 보세요."
        const val HINT_NICKNAME_CHANGE = "*닉네임은 30일마다 변경 가능해요."
        const val LOGOUT_DIALOG_TITLE = "로그아웃 하시겠어요?"
        const val LOGOUT_DIALOG_DESCRIPTION = "아쉬워요\uD83D\uDE25\n언제든 다시 놀러오세요!"
        const val UNREGISTER_TITLE = "정말 핀업과\n함께하지 않으실건가요?"
        const val UNREGISTER_DESCRIPTION = "회원탈퇴시 삭제된 정보는 복구가 불가능합니다."
        const val UNREGISTER_CHECK_COMMENT = "안내사항을 모두 확인하였으며, 이에 동의합니다."

        const val UNREGISTER_DIALOG_TITLE = "정말 탈퇴하시겠습니까?"
        const val UNREGISTER_DIALOG_DESCRIPTION = "회원 탈퇴시, 이전까지 사용하셨던\n서비스 기록들을 더 이상\n사용하실 수 없습니다."
        const val UNREGISTER_COMPLETE = "그동안 핀업을 이용해주셔서\n감사합니다 :) \n다음에 또 만나요!"
    }

    object Pinch {
        const val PINCH_WRITE = "핀츠 작성"
        const val PINCH_DETAIL = "핀츠 상세"
        const val TITLE_HINT = "제목을 입력하세요"
        const val DESCRIPTION_HINT = "핀츠의 설명을 적어주세요(0/120)"
        const val CREATE_PINCH = "핀츠 등록하기"
        const val CREATE_MY_PINCH = "마이핀츠 작성"
        const val PLACE_LIST = "장소목록"
        const val EDIT_PINTS = "핀츠 수정"
        const val DELETE_PINTS = "핀츠 삭제"
        const val NO_PINLOG_DIALOG_TITLE = "핀로그가 작성되지 않은 장소입니다."
        const val NO_PINLOG_DIALOG_CONTENT = "핀로그 작성한 장소만 핀츠에 등록 할 수 있어요.\n핀로그를 등록할까요?"
        const val PINCH_DELETE_DIALOG_TITLE = "핀츠를 정말 삭제할건가요?"
        const val PINCH_DELETE_DIALOG_CONTENT = "한 번 삭제하면 되돌릴 수 없어요.\n삭제하시겠습니까?"
        const val PINTS_WRITE_COMPLETE_DIALOG_TITLE = "마이핀츠 작성 완료!"
        const val PINTS_WRITE_COMPLETE_DIALOG_CONTENT = "등록한 마이핀츠를\n확인하시겠어요?"
    }

    object Toast {
        const val DELETE_PINLOG = "핀로그가 삭제되었어요."
        const val DELETE_PIN_BUDDY = "해당 핀버디가 삭제되었어요"
        const val ACCEPT_PIN_BUDDY = "핀버디 신청을 수락했어요"
        const val REFUSE_PIN_BUDDY = "핀버디 신청을 거절했어요"
        const val CANCEL_PIN_BUDDY_REQEUST = "핀버디 신청을 취소했어요"
    }

    object Kakao {
        fun getProfileShareTitle(name: String) = "$name 님의 핀업 계정"
        fun getProfileShareContent(name: String) = "핀업에서 $name 님의 핀들을 구경해 보세요."
        const val PROFILE_SHARE_BUTTON = "핀업으로 이동하기"
    }
}