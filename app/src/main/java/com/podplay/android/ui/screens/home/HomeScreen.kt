package com.podplay.android.ui.screens.home

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.data.model.PodcastSummaryViewData

@Composable
fun HomeRoute(
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen(uiState = viewModel.homeUiState, onPodcastClick = onPodcastClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(R.string.discover)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (uiState.showError) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Check your internet connection and try again")
                    }
                }
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    DiscoverPodcasts(
                        title = "Health",
                        podcasts = uiState.healthList,
                        context = context,
                        onPodcastClick = onPodcastClick,
                    )
                    DiscoverPodcasts(
                        title = "Self Improvement",
                        podcasts = uiState.selfImprovementList,
                        context = context,
                        onPodcastClick = onPodcastClick,
                    )
                    DiscoverPodcasts(
                        title = "Technology",
                        podcasts = uiState.techList,
                        context = context,
                        onPodcastClick = onPodcastClick,
                    )
                    DiscoverPodcasts(
                        title = "Business",
                        podcasts = uiState.businessList,
                        context = context,
                        onPodcastClick = onPodcastClick,
                    )
                    DiscoverPodcasts(
                        title = "Food",
                        podcasts = uiState.foodList,
                        context = context,
                        onPodcastClick = onPodcastClick,
                    )
                }
            }
        }
    }
}

@Composable
fun DiscoverPodcasts(
    title: String,
    podcasts: List<PodcastSummaryViewData>,
    context: Context,
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
) {
    Text(
        text = title,
        fontSize = 16.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp)
    )
    LazyRow {
        items(podcasts) { podcast ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(podcast.imageUrl)
                    .placeholder(R.drawable.logo)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.logo),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
                    .size(width = 106.dp, height = 130.dp)
                    .padding(start = 16.dp, top = 8.dp, bottom = 15.dp)
                    .clickable { onPodcastClick(podcast.feedUrl!!, podcast.imageUrl!!) }
            )
        }
    }
}
