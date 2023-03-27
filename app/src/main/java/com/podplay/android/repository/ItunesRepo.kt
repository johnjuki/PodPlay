package com.podplay.android.repository

import com.podplay.android.service.ItunesService

class ItunesRepo(private val itunesService: ItunesService) {
    suspend fun searchByTerm(term: String) = itunesService.searchPodcastByTerm(term)
}
