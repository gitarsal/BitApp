package com.project.bitapp.data.repositoryimpl

import com.project.bitapp.data.api.PairListingApi
import com.project.bitapp.domain.repository.PairListingRepo
import com.project.bitapp.utils.getAllPairsUrl
import javax.inject.Inject

class PairListingRepoImpl @Inject constructor(
    private val getPairListingApi: PairListingApi

) : PairListingRepo {
    override suspend fun getAllPairNameList(): List<String>? {

        try {
            val responseBody = getPairListingApi.getAllPairNameListService()
            responseBody?.let { list ->
                return if (list.isNotEmpty()) {
                    responseBody[0]
                } else {
                    null
                }
            }
        } catch (ee: java.lang.Exception) {
            return null
        }
        return null
    }

    override suspend fun getAllPairList(): List<List<String>?>? {
        val list = getAllPairNameList()
        list?.let { li ->
            try {
                val url = getAllPairsUrl(li)
                val responseBody = getPairListingApi.getAllPairListingService(url = url)

                responseBody?.let { list ->
                    return if (list.isNotEmpty()) {
                        return list
                    } else {
                        null
                    }
                }
            } catch (ee: java.lang.Exception) {
                return null
            }
            return null
        }

        return null
    }
}
