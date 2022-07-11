package com.tenqube.webui.component.noticatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenqube.webui.component.noticatch.dto.NotificationAppDto
import kotlinx.coroutines.launch

class NotiCatchViewModel(
    private val resourceAppService: ResourceAppService
    ) : ViewModel() {

    private val _apps = MutableLiveData<List<NotificationAppDto>>()
    val apps: LiveData<List<NotificationAppDto>> = _apps

    fun loadApps() {
        viewModelScope.launch {
            val results = resourceAppService.getNotiCatchApps()
            _apps.value = results.map {
                NotificationAppDto(it.name, it.image)
            }
        }
    }

}