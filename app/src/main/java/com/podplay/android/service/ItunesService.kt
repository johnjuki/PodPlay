package com.podplay.android.service

interface ItunesService {
    @GET("/search?media=podcast")
    suspend fun searchPodcastByTerm(@Query("term") term: String) : Response<PodcastResponse>

    companion object {
        val instance: ItunesService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(ItunesService::class.java)
        }
    }
}