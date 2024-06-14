package com.michaeltchuang.example.data.repositories

import com.michaeltchuang.example.data.models.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow

class NetworkRepository(private val httpClient: HttpClient) {
    suspend fun getProductList(): Flow<NetworkResult<ApiResponse?>> {
        return toResultFlow {
            val response = httpClient.get("https://dummyjson.com/products?limit=100&skip=5").body<ApiResponse>()
            NetworkResult.Success(response)
        }
    }
}
