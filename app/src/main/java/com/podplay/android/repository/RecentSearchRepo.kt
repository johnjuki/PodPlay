package com.podplay.android.repository

import com.podplay.android.data.db.RecentSearchDao
import com.podplay.android.data.model.RecentSearch
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RecentSearchRepo {

    fun getAllRecentSearch(): Flow<List<RecentSearch>>

    suspend fun insertRecentSearch(recentSearch: RecentSearch)

    suspend fun deleteRecentSearch(searchTerm: String)

    suspend fun clearSearchHistory()
}

class RecentSearchRepoImpl @Inject constructor(
    private val recentSearchDao: RecentSearchDao,
) : RecentSearchRepo {

    override fun getAllRecentSearch(): Flow<List<RecentSearch>> = recentSearchDao.getAllRecentSearch()

    override suspend fun insertRecentSearch(recentSearch: RecentSearch) =
        recentSearchDao.insert(recentSearch)

    override suspend fun deleteRecentSearch(searchTerm: String) = recentSearchDao.delete(searchTerm)

    override suspend fun clearSearchHistory() = recentSearchDao.deleteAll()
}