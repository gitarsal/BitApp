package com.project.bitapp.domain.usecase

import com.project.bitapp.domain.model.AllPairItem
import com.project.bitapp.domain.repository.PairListingRepo
import com.project.bitapp.utils.toAllPairItem
import javax.inject.Inject

class GetPairListingUseCase @Inject constructor(
    private val getPairListingRepo: PairListingRepo

) {
    suspend fun getAllPairList(): List<AllPairItem>? {
        var data: List<AllPairItem>?
        val response = getPairListingRepo.getAllPairList()

        if (response.isNullOrEmpty()) {
            return null
        } else {

            data = response.map { resp ->
                resp!!.toAllPairItem()
            }
        }
        return data
    }
}
