package com.tenqube.shared.util

object Constants {

    const val NONE = "none"

    enum class SMSType {
        NOTIFICATION, MMS, SMS
    }

    enum class DWType {
        DEPOSIT, WITHDRAW
    }

    enum class CardType(val value: String) {
        CHECK("체크"), CARD("신용"), BANK_ACCOUNT("계좌"), CASH("현금")
    }

    enum class CardSubType {
        NORMAL, CORPORATION, FAMILY
    }

    enum class StatusCode(val code: Int) {
        Success(200),
        NeedToSync(200),
        EmptyData(100),

        Forbidden(403),
        ParameterError(400),
        JSONParsingError(400),

        ServerError(500);

        companion object {
            fun fromCode(code: Int): StatusCode {
                return values().find { it.code == code } ?: ServerError
            }
        }
    }
}
