package com.tenqube.visualbase.infrastructure.framework.di

import com.tenqube.visualbase.service.parser.ParserAppService

object ServiceLocator {

    private var parserAppService: ParserAppService? = null
    fun provideParserAppService(): ParserAppService {
        return parserAppService ?: createParserAppService()
    }

    private fun createParserAppService(): ParserAppService {
        return parserAppService!! // TODO 생성 모듈 만들기
    }
}
