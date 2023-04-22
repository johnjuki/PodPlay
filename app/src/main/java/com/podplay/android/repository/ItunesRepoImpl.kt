package com.podplay.android.repository

import com.podplay.android.service.ItunesService
import javax.inject.Inject

class ItunesRepoImpl @Inject constructor(private val itunesService: ItunesService) : ItunesRepo {

    override suspend fun searchByTerm(term: String) = itunesService.searchPodcastByTerm(term)

}
