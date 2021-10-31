package com.project.bitapp.data.api

import com.project.bitapp.utils.GET_ALL_PAIR_URL
import retrofit2.http.GET
import retrofit2.http.Url

interface PairListingApi {
    @GET(GET_ALL_PAIR_URL)
    suspend fun getAllPairNameListService(): List<List<String>?>?

    @GET
    suspend fun getAllPairListingService(
        @Url() url: String
    ): List<List<String>?>?
}
