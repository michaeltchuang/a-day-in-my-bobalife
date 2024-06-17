package com.michaeltchuang.example.ui.viewmodels

import androidx.lifecycle.viewModelScope
import com.michaeltchuang.example.data.models.Validator
import com.michaeltchuang.example.data.repositories.ApiStatus
import com.michaeltchuang.example.data.repositories.NetworkResult
import com.michaeltchuang.example.data.repositories.ValidatorRepository
import com.michaeltchuang.example.utils.Constants
import com.michaeltchuang.example.utils.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ValidatorsListViewModel(private val validatorRepository: ValidatorRepository) : BaseViewModel(validatorRepository) {
    override val TAG: String = "ValidatorsListViewModel"

    private val _validatorsListState = MutableStateFlow(ValidatorsListState())
    private val _validatorsListViewState: MutableStateFlow<ValidatorsListScreenState> = MutableStateFlow(ValidatorsListScreenState.Loading)
    val validatorsListViewState = _validatorsListViewState.asStateFlow()
    var abiContract = "ValidatorRegistry.arc4.json"

    suspend fun fetchValidatorCount() {

        viewModelScope.launch {
            val acct = account
            acct?.apply {
                val validatorCount =
                    validatorRepository.getNumberOfValidators(
                        acct,
                        Constants.RETI_APP_ID_TESTNET,
                        abiContract
                    )
                Log.e(TAG, "validatorCount is $validatorCount")
            }
        }
    }
    suspend fun getValidators() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                validatorRepository.getValidatorsList().collect { response: NetworkResult<List<Validator>> ->
                    when (response.status) {
                        ApiStatus.LOADING -> {
                            _validatorsListState.update { it.copy(isLoading = true) }
                        }
                        ApiStatus.SUCCESS -> {
                            _validatorsListState.update { it.copy(isLoading = false, errorMessage = "", response.data) }
                        }
                        ApiStatus.ERROR -> {
                            _validatorsListState.update { it.copy(isLoading = false, errorMessage = response.message) }
                        }
                    }
                }
            } catch (e: Exception) {
                _validatorsListState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
            _validatorsListViewState.value = _validatorsListState.value.toUiState()
        }
    }

    sealed class ValidatorsListScreenState {
        data object Loading : ValidatorsListScreenState()

        data class Error(val errorMessage: String) : ValidatorsListScreenState()

        data class Success(val responseData: List<Validator>) : ValidatorsListScreenState()
    }

    private data class ValidatorsListState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        var responseData: List<Validator>? = null,
    ) {
        fun toUiState(): ValidatorsListScreenState {
            return if (isLoading) {
                ValidatorsListScreenState.Loading
            } else if (errorMessage?.isNotEmpty() == true) {
                ValidatorsListScreenState.Error(errorMessage)
            } else {
                ValidatorsListScreenState.Success(responseData!!)
            }
        }
    }
}