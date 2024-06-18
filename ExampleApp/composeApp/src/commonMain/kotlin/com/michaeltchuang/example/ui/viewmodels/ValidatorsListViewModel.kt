package com.michaeltchuang.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.data.repositories.ValidatorRepository
import com.michaeltchuang.example.utils.Constants
import com.michaeltchuang.example.utils.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ValidatorsListUIState {
    object Loading : ValidatorsListUIState()

    data class Error(
        val message: String,
    ) : ValidatorsListUIState()

    data class Success(
        val result: List<ValidatorEntity>,
    ) : ValidatorsListUIState()
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
open class ValidatorsListViewModel(
    private val validatorRepository: ValidatorRepository,
) : ViewModel() {
    val TAG: String = "ValidatorsListViewModel"
    var algorandBaseViewModel: AlgorandBaseViewModel?? = null

    // private val _validatorsListState = MutableStateFlow(ValidatorsListState())
    // private val _validatorsListViewState: MutableStateFlow<ScreenState> = MutableStateFlow(ScreenState.Loading)
    // val validatorsListViewState = _validatorsListViewState.asStateFlow()
    var abiContract = "ValidatorRegistry.arc4.json"

    val validatorsFromDb =
        validatorRepository
            .getAllValidatorsFromDb()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val searchQuery = MutableStateFlow("")
    val validatorsListUIState: StateFlow<ValidatorsListUIState> =
        searchQuery
            .debounce(250)
            .flatMapLatest { searchQuery ->
                validatorsFromDb.mapLatest { validatorList ->
                    if (validatorList.isNotEmpty()) {
                        val validators =
                            validatorList
                                .filter { it.name.contains(searchQuery, ignoreCase = true) }
                                .sortedByDescending { it.name }
                        ValidatorsListUIState.Success(validators)
                    } else {
                        ValidatorsListUIState.Loading
                    }
                }
            }.stateIn(viewModelScope, SharingStarted.Lazily, ValidatorsListUIState.Loading)

    fun onValidatorSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    suspend fun fetchValidatorCount() {
        viewModelScope.launch {
            val acct = algorandBaseViewModel?.account
            acct?.apply {
                val validatorCount =
                    validatorRepository.getNumberOfValidators(
                        acct,
                        Constants.RETI_APP_ID_TESTNET,
                        abiContract,
                    )
                Log.e(TAG, "validatorCount is $validatorCount")
            }
        }
    }

//    suspend fun getValidators() {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                validatorRepository.getValidatorsList().collect { response: NetworkResult<List<Validator>> ->
//                    when (response.status) {
//                        ApiStatus.LOADING -> {
//                            _validatorsListState.update { it.copy(isLoading = true) }
//                        }
//                        ApiStatus.SUCCESS -> {
//                            _validatorsListState.update { it.copy(isLoading = false, errorMessage = "", response.data) }
//                        }
//                        ApiStatus.ERROR -> {
//                            _validatorsListState.update { it.copy(isLoading = false, errorMessage = response.message) }
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                _validatorsListState.update { it.copy(isLoading = false, errorMessage = e.message) }
//            }
//            _validatorsListViewState.value = _validatorsListState.value.toUiState()
//        }
//    }
}
