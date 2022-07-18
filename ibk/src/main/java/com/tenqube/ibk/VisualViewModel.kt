package com.tenqube.ibk

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tenqube.ibk.bridge.dto.request.*
import com.tenqube.ibk.bridge.dto.response.BanksDto
import com.tenqube.ibk.bridge.dto.response.TransactionDto
import com.tenqube.ibk.bridge.dto.response.TransactionsResponse
import com.tenqube.ibk.service.card.CardAppService
import com.tenqube.visualbase.service.transaction.dto.TransactionFilter
import com.tenqube.ibk.service.user.UserAppService
import com.tenqube.visualbase.service.transaction.TransactionAppService
import com.tenqube.webui.UIService
import com.tenqube.webui.dto.OpenSelectBox
import com.tenqube.webui.dto.SelectBoxItem
import com.tenqube.webui.dto.SelectBoxRequest
import kotlinx.coroutines.launch

class VisualViewModel(
    private val transactionAppService: TransactionAppService,
    private val cardAppService: CardAppService,
    private val userAppService: UserAppService,
    private val uiService: UIService
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    private val _banks = MutableLiveData<BanksDto>()
    val banks: LiveData<BanksDto> = _banks

    private val _showAd = MutableLiveData<ShowAdDto>()
    val showAd: LiveData<ShowAdDto> = _showAd

    private val _hideAd = MutableLiveData<Unit>()
    val hideAd: LiveData<Unit> = _hideAd

    private val _transactions = MutableLiveData<TransactionsResponse>()
    val transactions: LiveData<TransactionsResponse> = _transactions

    fun start(url: String) {
        _url.value = url
    }

    fun openNotiSettings() {
        uiService.openNotiSettings()
    }

    fun openDeepLink(request: OpenDeepLinkDto) {
        uiService.openNewView(
            com.tenqube.webui.dto.OpenNewViewDto(
                "external",
                request.url
            )
        )
    }

    fun showToast(request: String) {
        uiService.showToast(request)
    }

    fun openSelectBox(request: OpenSelectBoxDto,
                      callback: (selectBox: SelectBoxItem) -> Unit) {
        uiService.openSelectBox(OpenSelectBox(
            SelectBoxRequest(
                request.title,
                request.selectedColor,
                request.data.map {
                    SelectBoxItem(
                        it.name, it.orderByType, it.isSelected
                    )
                }
            )
        ){
            callback(it)
        })
    }

    fun showAd(request: ShowAdDto) {
        _showAd.value = request
    }

    fun hideAd() {
        _hideAd.value = Unit
    }

    fun openNewView(request: OpenNewViewDto) {
        uiService.openNewView(com.tenqube.webui.dto.OpenNewViewDto(
            request.type, request.url
        ))
    }

    fun finish() {
        uiService.finish()
    }

    fun getBanks() {
        viewModelScope.launch {
            _banks.value = BanksDto(listOf())
        }
    }

    fun getTransactions(request: GetTransactionsDto) {
        viewModelScope.launch {
            val transactions = transactionAppService.getTransactions(
                TransactionFilter(
                    request.year,
                    request.month,
                    request.periodByMonth
                )
            ).getOrDefault(listOf())

            _transactions.value = TransactionsResponse(transactions.map {
                TransactionDto.fromDomain(it)
            })
        }
    }
}
