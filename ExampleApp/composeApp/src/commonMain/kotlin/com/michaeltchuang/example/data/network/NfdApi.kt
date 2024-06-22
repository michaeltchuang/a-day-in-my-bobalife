package com.michaeltchuang.example.data.network


import com.michaeltchuang.example.data.model.NfdRecord
import com.michaeltchuang.example.data.models.Valid
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.component.KoinComponent


class NfdApi(
    private val client: HttpClient,
    private val baseUrl: String = "https://api.testnet.nf.domains",
) : KoinComponent {

    suspend fun isValid(appId: Int) = client.get("$baseUrl/nfd/isValid/$appId").body<Valid>()
    suspend fun fetchNfd(nfd: String) = client.get("$baseUrl/nfd/$nfd").body<NfdRecord>()
}