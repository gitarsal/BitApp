package com.project.bitapp.domain.repository

interface PairListingRepo {

    suspend fun getAllPairNameList(): List<String>?

    suspend fun getAllPairList(): List<List<String>?>?
}
