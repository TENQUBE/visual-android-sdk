package com.tenqube.ibk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenqube.ibk.bridge.dto.request.*
import com.tenqube.ibk.bridge.dto.response.BanksDto
import com.tenqube.ibk.bridge.dto.response.TransactionsResponse
import com.tenqube.ibk.service.CardAppService
import com.tenqube.ibk.service.TransactionAppService
import com.tenqube.ibk.service.UserAppService
import com.tenqube.webui.UIService
import kotlinx.coroutines.launch

class VisualViewModel(
    private val transactionAppService: TransactionAppService,
    private val cardAppService: CardAppService,
    private val userAppService: UserAppService,
    private val uiService: UIService
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    private val _openNotiSettings = MutableLiveData<Unit>()
    val openNotiSettings: LiveData<Unit> = _openNotiSettings

    private val _banks = MutableLiveData<BanksDto>()
    val banks: LiveData<BanksDto> = _banks

    private val _openDeepLink = MutableLiveData<OpenDeepLinkDto>()
    val openDeepLink: LiveData<OpenDeepLinkDto> = _openDeepLink

    private val _showToast = MutableLiveData<String>()
    val showToast: LiveData<String> = _showToast

    private val _openSelectBox = MutableLiveData<OpenSelectBoxDto>()
    val openSelectBox: LiveData<OpenSelectBoxDto> = _openSelectBox

    private val _showAd = MutableLiveData<ShowAdDto>()
    val showAd: LiveData<ShowAdDto> = _showAd

    private val _hideAd = MutableLiveData<Unit>()
    val hideAd: LiveData<Unit> = _hideAd

    private val _openNewView = MutableLiveData<OpenNewViewDto>()
    val openNewView: LiveData<OpenNewViewDto> = _openNewView

    private val _finish = MutableLiveData<Unit>()
    val finish: LiveData<Unit> = _finish

    private val _transactions = MutableLiveData<TransactionsResponse>()
    val transactions: LiveData<TransactionsResponse> = _transactions

    private val _isPageLoaded = MutableLiveData<Boolean>()
    val isPageLoaded: LiveData<Boolean> = _isPageLoaded

    fun setPageLoaded(isPageLoaded: Boolean) {
        this._isPageLoaded.value = isPageLoaded
    }

    fun start(url: String) {
        _url.value = url
    }

    fun openNotiSettings() {
        _openNotiSettings.value = Unit
    }

    fun getBanks() {
        viewModelScope.launch {
            _banks.value = BanksDto(listOf())
        }
    }

    fun openDeepLink(request: OpenDeepLinkDto) {
        _openDeepLink.value = request
    }

    fun showToast(request: String) {
        _showToast.value = request
    }

    fun openSelectBox(request: OpenSelectBoxDto) {
        _openSelectBox.value = request
    }

    fun showAd(request: ShowAdDto) {
        _showAd.value = request
    }

    fun hideAd() {
        _hideAd.value = Unit
    }

    fun openNewView(request: OpenNewViewDto) {
        _openNewView.value = request
    }

    fun finish() {
        _finish.value = Unit
    }

    fun getTransactions(request: GetTransactionsDto) {
        viewModelScope.launch {
            _transactions.value = TransactionsResponse(listOf())
        }
    }
}
