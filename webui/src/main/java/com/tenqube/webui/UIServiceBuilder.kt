package com.tenqube.webui

class UIServiceBuilder {

    fun create(
        viewComponent: ViewComponent
    ) {
        UiServiceImpl(
            viewComponent.activity,
            viewComponent.webView
        )
    }
}
