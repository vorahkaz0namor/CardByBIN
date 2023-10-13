package ru.cft.cardbybin.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.cft.cardbybin.dto.Card

interface CardRemoteApi {
    @GET("{bin}")
    suspend fun getCard(
        @Path("bin") bin: Int
    ): Response<Card>
}