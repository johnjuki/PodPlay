package com.podplay.android.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET("/search?media=podcast")
    suspend fun searchPodcastByTerm(@Query("term") term: String) : Response<PodcastResponse>
}
