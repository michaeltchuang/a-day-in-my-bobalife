package com.michaeltchuang.example.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algorand.algosdk.account.Account
import com.michaeltchuang.example.data.repositories.AlgorandRepository
import com.michaeltchuang.example.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

open class AlgorandBaseViewModel(
    private val repository: AlgorandRepository,
) : ViewModel() {
    open val TAG: String = "AlgorandBaseViewModel"

    var account: Account? = null
    var _account = MutableStateFlow<Account?>(null)
    var accountStateFlow = _account.asStateFlow()

    var _appOptInState = MutableStateFlow<Boolean>(false)
    var appOptInStateFlow = _appOptInState.asStateFlow()

    var _snackBarMessage = MutableStateFlow<String>("")
    var snackBarStateFlow = _snackBarMessage.asStateFlow()

    var accountInfo: com.algorand.algosdk.v2.client.model.Account? = null
    var hasExistingBet = false

    fun createAccount() {
        viewModelScope.launch {
            val result = repository.generateAlgodPair()
            result.let {
                _account.value = it
                account = it
            }
        }
    }

    fun recoverAccount(
        passphrase: String?,
        appOptInStateCheck: Boolean,
    ) {
        if (passphrase == null) {
            account = null
            _account.value = null
        }

        passphrase?.apply {
            viewModelScope.launch {
                val result = repository.recoverAccount(passphrase)
                result?.let { r ->
                    _account.value = r
                    account = r
                    accountInfo = r.let { repository.getAccountInfo(it) }

                    // auto opt into app if account exists
                    if (appOptInStateCheck) {
                        appOptInStateCheck(r, Constants.COINFLIP_APP_ID_TESTNET)
                    }
                }
            }
        }
    }

    fun appOptInStateCheck(
        account: Account,
        appId: Long,
    ) {
        viewModelScope.launch {
            try {
                var optInAlready = false
                hasExistingBet = false
                val accountInfo = repository.getAccountInfo(account)

                // skip if opt in already
                accountInfo?.appsLocalState?.forEach {
                    if (it.id == appId) {
                        Log.d(TAG, String.format("Account has already opt in to app id: " + appId))
                        optInAlready = true
                        if (it.keyValue.size > 0) {
                            hasExistingBet = true
                        }
                    }
                }
                Log.d(TAG, "account app ($appId) - opt in state($optInAlready), has existing bet($hasExistingBet)")
                _appOptInState.value = optInAlready
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun appOptIn(
        account: Account,
        appId: Long,
    ) {
        viewModelScope.launch {
            try {
                val res = repository.appOptIn(account, appId)
                if (res?.confirmedRound != null) {
                    appOptInStateCheck(account, appId)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun closeOutApp(
        account: Account,
        appId: Long,
    ) {
        viewModelScope.launch {
            try {
                val res = repository.closeOutApp(account, appId)
                if (res?.confirmedRound != null) {
                    appOptInStateCheck(account, appId)
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
    }

    fun setSnackBarMessage(resource: StringResource) {
        viewModelScope.launch {
            _snackBarMessage.value = getString(resource = resource)
        }
    }

    fun setSnackBarMessage(str: String) {
        viewModelScope.launch {
            _snackBarMessage.value = str
        }
    }
}

sealed class ScreenState {
    data object Loading : ScreenState()

    data class Error(
        val errorMessage: String,
    ) : ScreenState()

    data class Success(
        val responseData: Any,
    ) : ScreenState()
}
