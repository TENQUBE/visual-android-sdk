package com.tenqube.ibk

import android.view.View
import androidx.lifecycle.*
import com.tenqube.ibk.bridge.dto.request.*
import com.tenqube.ibk.bridge.dto.response.*
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
import com.tenqube.webui.dto.ShowAd
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class VisualViewModel(
    private val userAppService: UserAppService,
    private val transactionAppService: TransactionAppService,
    private val cardAppService: CardAppService,
    private val uiService: UIService,
    private val bulkParserAppService: BulkParserAppService,
    private val ibkSharedPreference: IBKSharedPreference
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    val url: LiveData<String> = _url

    private val _showAd = MutableLiveData<View>()
    val showAd: LiveData<View> = _showAd

    private val _hideAd = MutableLiveData<Unit>()
    val hideAd: LiveData<Unit> = _hideAd

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _isProgress = MutableLiveData<Boolean>()
    val isProgress: LiveData<Boolean> = _isProgress

    private val _progressCount = MutableLiveData<ProgressCount>()
    val progressCount: LiveData<ProgressCount> = _progressCount

    private val _refreshEnabled = MutableLiveData<Boolean>()
    val refreshEnabled: LiveData<Boolean> = _refreshEnabled

    fun start(url: String) {
        _url.value = url
    }

    fun start(url: String, user: CreateUser) {
        viewModelScope.launch {
            try {
                userAppService.signUp(user).getOrThrow()
                ibkSharedPreference.disableTranPopup()
                _url.value = VisualFragment.PROGRESS_URL
                startBulk()
            } catch (e: Exception) {
                _url.value = url
            }
        }
    }

    private suspend fun startBulk() {
        bulkParserAppService.start(
            BulkSmsAdapterImpl(bulkParserAppService, object : BulkCallback {
                override fun onStart() {
                    _isProgress.postValue(true)
                }
                override fun onProgress(now: Int, total: Int) {
                    _progressCount.postValue(ProgressCount(now, total))
                }
                override fun onCompleted() {
                    _isProgress.postValue(false)
                }
                override fun onError(code: Int) {
                    _error.postValue("내역을 불러오던 도중 에러가 발생하였습니다.")
                    _isProgress.postValue(false)
                }
            })
        )
    }

    fun openNotiSettings() {
        uiService.openNotiSettings()
    }

    fun openDeepLink(request: OpenDeepLinkRequest) {
        uiService.openNewView(
            request.asDomain()
        )
    }

    fun showToast(request: String) {
        uiService.showToast(request)
    }

    fun setRefreshEnabled(request: Boolean) {
        _refreshEnabled.value = request
    }

    fun setNotiEnabled(enabled: Boolean) {
        userAppService.setNotiEnabled(enabled)
    }

    fun openSelectBox(request: OpenSelectBoxRequest, callback: (selectBox: SelectBoxItem) -> Unit) {
        uiService.openSelectBox(OpenSelectBox(
            request.asDomain(), callback
        ))
    }

    fun showAd(request: ShowAdDto) {
        uiService.showAd(ShowAd(
            request.unitId,
            request.container.asDomain(),
            request.button.asDomain(),
            callback = {
                _showAd.value = it
            }
        ))
    }

    fun hideAd() {
        _hideAd.value = Unit
    }

    fun openNewView(request: OpenNewViewRequest) {
        uiService.openNewView(request.asDomain())
    }

    fun finish() {
        uiService.finish()
    }

    fun getBanks() : BanksDto = runBlocking {
        return@runBlocking try {
            val cards = cardAppService.getCards().getOrThrow()
            BanksDto(cards.map {
                BankDto.fomDomain(it)
            })
        } catch (e: Exception) {
            BanksDto(listOf())
        }
    }

    fun getNotiBanks() : NotiBanksDto = runBlocking {
        val countByCard = transactionAppService.getCountByNoti().getOrDefault(listOf())
        return@runBlocking NotiBanksDto(countByCard.map {
            NotiBankDto.fromDomain(it)
        })
    }

    fun getTransactions(request: GetTransactionsRequest) : TransactionsResponse = runBlocking {
        return@runBlocking try {
            val transactions = transactionAppService.getTransactions(
                TransactionFilter(
                    request.year,
                    request.month,
                )
            ).getOrThrow()
            TransactionsResponse(transactions.map {
                TransactionDto.fromDomain(it)
            })
        } catch (e: Exception) {
            TransactionsResponse(listOf())
        }
    }

    class Factory(
        private val userAppService: UserAppService,
        private val transactionAppService: TransactionAppService,
        private val cardAppService: CardAppService,
        private val uiService: UIService,
        private val bulkParserAppService: BulkParserAppService,
        private val ibkSharedPreference: IBKSharedPreference
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VisualViewModel(
                userAppService,
                transactionAppService,
                cardAppService,
                uiService,
                bulkParserAppService,
                ibkSharedPreference) as T
        }
    }
}