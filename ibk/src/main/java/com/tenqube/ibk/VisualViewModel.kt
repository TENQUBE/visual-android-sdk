package com.tenqube.ibk

import androidx.lifecycle.*
import com.tenqube.ibk.bridge.dto.request.*
import com.tenqube.ibk.bridge.dto.response.BankDto
import com.tenqube.ibk.bridge.dto.response.BanksDto
import com.tenqube.ibk.bridge.dto.response.TransactionDto
import com.tenqube.ibk.bridge.dto.response.TransactionsResponse
import com.tenqube.ibk.progress.ProgressCount
import com.tenqube.visualbase.domain.user.command.CreateUser
import com.tenqube.visualbase.service.card.CardAppService
import com.tenqube.visualbase.service.parser.BulkCallback
import com.tenqube.visualbase.service.parser.BulkParserAppService
import com.tenqube.visualbase.service.parser.BulkSmsAdapterImpl
import com.tenqube.visualbase.service.transaction.TransactionAppService
import com.tenqube.visualbase.service.transaction.dto.TransactionFilter
import com.tenqube.visualbase.service.user.UserAppService
import com.tenqube.webui.UIService
import com.tenqube.webui.dto.OpenSelectBox
import com.tenqube.webui.dto.SelectBoxItem
import com.tenqube.webui.dto.SelectBoxRequest
import kotlinx.coroutines.launch

class VisualViewModel(
    private val userAppService: UserAppService,
    private val transactionAppService: TransactionAppService,
    private val cardAppService: CardAppService,
    private val uiService: UIService,
    private val bulkParserAppService: BulkParserAppService
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _selectBoxItem = MutableLiveData<SelectBoxItem>()
    val selectBoxItem: LiveData<SelectBoxItem> = _selectBoxItem

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress: LiveData<Boolean> = _isProgress

    private val _progressCount = MutableLiveData<ProgressCount>()
    val progressCount: LiveData<ProgressCount> = _progressCount

    fun start(url: String, user: CreateUser) {
        viewModelScope.launch {
            try {
                userAppService.signUp(user).getOrThrow()

                BulkSmsAdapterImpl(bulkParserAppService, object : BulkCallback {
                    override fun onStart() {
                        _isProgress.value = true
                    }

                    override fun onProgress(now: Int, total: Int) {
                        _progressCount.value = ProgressCount(now, total)
                    }

                    override fun onCompleted() {
                        _isProgress.value = false
                    }

                    override fun onError(code: Int) {
                        _error.value = "내역을 불러오던 도중 에러가 발생하였습니다."
                        _isProgress.value = false
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _url.value = url
            }
        }
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

    fun openSelectBox(request: OpenSelectBoxDto) {
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
            _selectBoxItem.value = it
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
            try {
                val cards = cardAppService.getCards().getOrThrow()
                _banks.value = BanksDto(cards.map {
                    BankDto.fomDomain(it)
                })
            } catch (e: Exception) {
                _error.value = e.toString()
            }
        }
    }

    fun getTransactions(request: GetTransactionsDto) {
        viewModelScope.launch {
            try {
                val transactions = transactionAppService.getTransactions(
                    TransactionFilter(
                        request.year,
                        request.month,
                        request.periodByMonth
                    )
                ).getOrThrow()

                _transactions.value = TransactionsResponse(transactions.map {
                    TransactionDto.fromDomain(it)
                })
            } catch (e: Exception) {
                _error.value = e.toString()
            }
        }
    }

    class Factory(
        private val userAppService: UserAppService,
        private val transactionAppService: TransactionAppService,
        private val cardAppService: CardAppService,
        private val uiService: UIService,
        private val bulkParserAppService: BulkParserAppService
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VisualViewModel(
                userAppService,
                transactionAppService,
                cardAppService,
                uiService,
                bulkParserAppService) as T
        }
    }
}