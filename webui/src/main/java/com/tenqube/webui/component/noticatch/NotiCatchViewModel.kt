package com.tenqube.webui.component.noticatch

import androidx.lifecycle.*
import com.tenqube.webui.component.noticatch.dto.NotificationAppDto
import kotlinx.coroutines.launch

class NotiCatchViewModelFactory(private val resourceAppService: ResourceAppService): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = NotiCatchViewModel(resourceAppService) as T
}

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