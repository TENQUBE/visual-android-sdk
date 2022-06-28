package com.tenqube.shared.util

interface Callback<T> {
    fun onDataLoaded(value: T)
}
