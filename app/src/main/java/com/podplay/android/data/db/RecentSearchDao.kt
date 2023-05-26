package com.podplay.android.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.podplay.android.data.model.RecentSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Query("SELECT * FROM recent_search GROUP BY search_term ORDER BY id DESC LIMIT 5")
    fun getAllRecentSearch() : Flow<List<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearch: RecentSearch)

    @Query("DELETE FROM recent_search WHERE search_term = :searchTerm")
    suspend fun delete(searchTerm: String)

    @Query("DELETE FROM recent_search")
    suspend fun deleteAll()
}
