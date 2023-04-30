package com.podplay.android.ui

//class PodcastActivity : AppCompatActivity(), PodcastListAdapter.PodcastListAdapterListener,
//    PodcastDetailsFragment.OnPodcastDetailsListener {
//
//    private val searchViewModel by viewModels<SearchViewModel>()
//    private val podcastViewModel by viewModels<PodcastViewModel>()
//    private lateinit var searchMenuItem: MenuItem
//    private lateinit var databinding: ActivityPodcastBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        databinding = ActivityPodcastBinding.inflate(layoutInflater)
//        setContentView(databinding.root)
//        setupToolbar()
////        setupViewModels()
//        setupPodcastListView()
//        handleIntent(intent)
//        addBackStackListener()
//        scheduleJobs()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val inflater = menuInflater
//        inflater.inflate(R.menu.menu_search, menu)
//
//        searchMenuItem = menu.findItem(R.id.search_item)
//        val searchView = searchMenuItem.actionView as SearchView
//
//        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
//            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
//                return true
//            }
//
//            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
//                showSubscribedPodcasts()
//                return true
//            }
//        })
//
//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
//
//        if (supportFragmentManager.backStackEntryCount > 0) {
//            databinding.podcastListComposeView.visibility = View.INVISIBLE
//        }
//
//        if (databinding.podcastListComposeView.visibility == View.INVISIBLE) {
//            searchMenuItem.isVisible = false
//        }
//
//        return true
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//        setIntent(intent)
//        handleIntent(intent)
//    }
//
//
//    override fun onShowDetails(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
//        podcastSummaryViewData.feedUrl ?: return
//        showProgressBar()
//        podcastViewModel.viewModelScope.launch(context = Dispatchers.Main) {
//            podcastViewModel.getPodcast(podcastSummaryViewData)
//            hideProgressBar()
//            showDetailsFragment()
//        }
//    }
//
//    private fun scheduleJobs() {
//        val constraints: Constraints = Constraints.Builder().apply {
//            setRequiredNetworkType(NetworkType.CONNECTED)
//            setRequiresCharging(true)
//        }.build()
//
//        val request = PeriodicWorkRequestBuilder<EpisodeUpdateWorker>(
//            1, TimeUnit.HOURS
//        )
//            .setConstraints(constraints)
//            .build()
//
//        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
//            TAG_EPISODE_UPDATE_JOB,
//            ExistingPeriodicWorkPolicy.REPLACE, request
//        )
//    }
//
//    private fun showSubscribedPodcasts() {
//        val podcasts = podcastViewModel.getPodcasts()?.value
//
////        if (podcasts != null) {
////            databinding.toolbar.title = getString(R.string.subscribed_podcasts)
////            databinding.podcastListComposeView.apply {
////                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
////                setContent {
////                    PodcastList(podcastList = podcasts, onItemClick = { podcastSummaryViewData ->
////                        podcastSummaryViewData.feedUrl ?: return@PodcastList
////                        showProgressBar()
////                        podcastViewModel.viewModelScope.launch(context = Dispatchers.Main) {
////                            podcastViewModel.getPodcast(podcastSummaryViewData)
////                            hideProgressBar()
////                            showDetailsFragment()
////                        }
////                    })
////                }
////            }
////        }
//    }
//
//    @OptIn(DelicateCoroutinesApi::class)
//    private fun performSearch(term: String) {
//        showProgressBar()
//        GlobalScope.launch {
////            val results = searchViewModel.searchPodcasts(term)
//            withContext(Dispatchers.Main) {
//                hideProgressBar()
//                databinding.toolbar.title = term
//                databinding.podcastListComposeView.apply {
//                    setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
////                    setContent {
////                        PodcastList(podcastList = results, onItemClick = { podcastSummaryViewData ->
////                        podcastSummaryViewData.feedUrl ?: return@PodcastList
////                        showProgressBar()
////                        podcastViewModel.viewModelScope.launch(context = Dispatchers.Main) {
////                            podcastViewModel.getPodcast(podcastSummaryViewData)
////                            hideProgressBar()
////                            showDetailsFragment()
////                        }
////                        })
////                    }
//                }
//            }
//        }
//    }
//
//    private fun handleIntent(intent: Intent) {
//        if (Intent.ACTION_SEARCH == intent.action) {
//            val query = intent.getStringExtra(SearchManager.QUERY) ?: return
//            performSearch(query)
//        }
//        val podcastFeedUrl = intent.getStringExtra(EpisodeUpdateWorker.EXTRA_FEED_URL)
//        if (podcastFeedUrl != null) {
//            podcastViewModel.viewModelScope.launch {
//                val podcastSummaryViewData = podcastViewModel.setActivePodcast(podcastFeedUrl)
//                podcastSummaryViewData?.let { podcastSummaryView: SearchViewModel.PodcastSummaryViewData ->
//                    onShowDetails(
//                        podcastSummaryView
//                    )
//                }
//            }
//        }
//    }
//
//
//    private fun setupToolbar() {
//        setSupportActionBar(databinding.toolbar)
//    }
//
////    private fun setupViewModels() {
////        val service = ItunesService.instance
////        searchViewModel.iTunesRepoImpl = ItunesRepoImpl(service)
////        val rssService = RssFeedService.instance
////        podcastViewModel.podcastRepoImpl = PodcastRepoImpl(rssService, podcastViewModel.podcastDao)
////    }
//
//    private fun setupPodcastListView() {
//        podcastViewModel.getPodcasts()?.observe(this) {
//            if (it != null) {
//                showSubscribedPodcasts()
//            }
//        }
//    }
//
//    private fun addBackStackListener() {
//        supportFragmentManager.addOnBackStackChangedListener {
//            if (supportFragmentManager.backStackEntryCount == 0) {
//                databinding.podcastListComposeView.visibility = View.VISIBLE
//            }
//        }
//    }
//
//
//    private fun showDetailsFragment() {
//        val podcastDetailsFragment = createPodcastDetailsFragment()
//
//        supportFragmentManager.beginTransaction().add(
//            R.id.podcastDetailsContainer,
//            podcastDetailsFragment, TAG_DETAILS_FRAGMENT
//        ).addToBackStack("DetailsFragment").commit()
//        databinding.podcastListComposeView.visibility = View.INVISIBLE
//        searchMenuItem.isVisible = false
//    }
//
//    private fun showPlayerFragment() {
//        val episodePlayerFragment = createEpisodePlayerFragment()
//
//        supportFragmentManager.beginTransaction().replace(
//            R.id.podcastDetailsContainer,
//            episodePlayerFragment, TAG_PLAYER_FRAGMENT
//        ).addToBackStack("PlayerFragment").commit()
//        databinding.podcastListComposeView.visibility = View.INVISIBLE
//        searchMenuItem.isVisible = false
//    }
//
//    private fun createEpisodePlayerFragment(): EpisodePlayerFragment {
//
//        var episodePlayerFragment = supportFragmentManager.findFragmentByTag(TAG_PLAYER_FRAGMENT) as
//                EpisodePlayerFragment?
//
//        if (episodePlayerFragment == null) {
//            episodePlayerFragment = EpisodePlayerFragment.newInstance()
//        }
//        return episodePlayerFragment
//    }
//
//    private fun createPodcastDetailsFragment(): PodcastDetailsFragment {
//        var podcastDetailsFragment =
//            supportFragmentManager.findFragmentByTag(TAG_DETAILS_FRAGMENT) as PodcastDetailsFragment?
//
//        if (podcastDetailsFragment == null) {
//            podcastDetailsFragment = PodcastDetailsFragment.newInstance()
//        }
//
//        return podcastDetailsFragment
//    }
//
//    private fun showProgressBar() {
//        databinding.progressBar.visibility = View.VISIBLE
//    }
//
//    private fun hideProgressBar() {
//        databinding.progressBar.visibility = View.INVISIBLE
//    }
//
//    companion object {
//        private const val TAG_DETAILS_FRAGMENT = "DetailsFragment"
//        private const val TAG_EPISODE_UPDATE_JOB = "com.podplay.android.episodes"
//        private const val TAG_PLAYER_FRAGMENT = "PlayerFragment"
//    }
//
//    override fun onSubscribe() {
//        podcastViewModel.saveActivePodcast()
//        supportFragmentManager.popBackStack()
//    }
//
//    override fun onUnsubscribe() {
//        podcastViewModel.deleteActivePodcast()
//        supportFragmentManager.popBackStack()
//    }
//
//    override fun onShowEpisodePlayer(episodeViewData: EpisodeViewData) {
//        podcastViewModel.activeEpisodeViewData = episodeViewData
//        showPlayerFragment()
//    }
//}
