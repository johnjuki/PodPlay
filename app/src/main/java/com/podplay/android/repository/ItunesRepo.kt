package com.podplay.android.repository

import com.podplay.android.service.PodcastResponse
import retrofit2.Response

interface ItunesRepo {
    suspend fun searchByTerm(term: String): Response<PodcastResponse>
}