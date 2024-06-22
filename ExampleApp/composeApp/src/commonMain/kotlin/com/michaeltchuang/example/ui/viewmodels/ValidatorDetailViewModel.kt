package com.michaeltchuang.example.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.data.repositories.ValidatorRepository
import com.michaeltchuang.example.utils.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ValidatorDetailUIState {
    object Loading : ValidatorDetailUIState()

    data class Error(
        val message: String,
    ) : ValidatorDetailUIState()

    data class Success(
        val result: ValidatorEntity,
    ) : ValidatorDetailUIState()
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
open class ValidatorDetailViewModel(
    private val validatorRepository: ValidatorRepository,
) : ViewModel() {
    val TAG: String = "ValidatorsListViewModel"
    var algorandBaseViewModel: AlgorandBaseViewModel? = null

    // private val _validatorsListState = MutableStateFlow(ValidatorsListState())
    // private val _validatorsListViewState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    // val validatorsListViewState = _validatorsListViewState.asStateFlow()
    var abiContract = "ValidatorRegistry.arc4.json"

    var _validatorDetailUIStateFlow = MutableStateFlow<ValidatorDetailUIState>(ValidatorDetailUIState.Loading)
    var validatorDetailUIState = _validatorDetailUIStateFlow.asStateFlow()

    fun fetchValidatorById(validatorId: Int) {
        viewModelScope.launch {
            val validatorFromDb =
                validatorRepository
                    .getValidatorByIdFromDb(validatorId).collect { validator ->
                        Log.e(TAG, "validator result is $validator")
                        if (validator == null) {
                            _validatorDetailUIStateFlow.value = ValidatorDetailUIState.Error("Validator does not exist")
                        } else {
                            _validatorDetailUIStateFlow.value = ValidatorDetailUIState.Success(validator)
                        }

                    }
                    //.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ValidatorDetailUIState.Loading)
        }
    }

    fun addStakeToValidator(
        validatorId: Int = 1,
        amount: Int = 5000,
    ) {
        viewModelScope.launch {
            val acct = algorandBaseViewModel?.account
            acct?.apply {
                val validatorCount =
//                    validatorRepository.addStakeToValidator(
//                        account = acct,
//                        contractStr = abiContract,
//                        validatorId = validatorId.toLong(),
//                        amount = amount,
//                    )

                validatorRepository.fetchValidatorInfo(
                        account = acct,
                        contractStr = abiContract,
                        validatorId = validatorId.toBigInteger(),
                    )
//                    validatorRepository.appOptIn(
//                        account = acct,
//                        appId = Constants.RETI_APP_ID_TESTNET,
//                    )
                Log.e(TAG, "fetched validator $validatorId info")
            }
        }
    }
}


