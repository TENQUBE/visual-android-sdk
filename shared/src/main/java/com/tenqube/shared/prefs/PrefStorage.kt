package com.tenqube.shared.prefs

interface PrefStorage {
    var accessToken: String
    var refreshToken: String
    var layer: String
    var apiKey: String
    var searchUrl: String
    var searchApiKey: String
    var resourceUrl: String
    var resourceApiKey: String
    var ruleVersion: Int
    var syncCnt: Int
    var service: String
    var lastRcsTime: Long
    var notiIcon: Int
    var notiColor: Int
    var notiChannelId: String
    var isNotiEnabled: Boolean
    var visualReceiptDeepLink: String
    var visualReceiptPopupDeepLink: String
}
