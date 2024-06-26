package com.michaeltchuang.example.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Valid(
    val isValid: Boolean,
    val name: String,
    val sigNameAddress: String? = null,
)