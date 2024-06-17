package com.michaeltchuang.example.ui.viewmodels

import com.michaeltchuang.example.data.repositories.AlgorandRepository

class LoginViewModel(
    private val repository: AlgorandRepository,
) : BaseViewModel(repository) {
    override val TAG: String = "LoginViewModel"
}
