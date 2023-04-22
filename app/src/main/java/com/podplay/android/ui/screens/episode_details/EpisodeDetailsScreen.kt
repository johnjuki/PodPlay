package com.podplay.android.ui.screens.episode_details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.util.HtmlUtils

@Composable
fun EpisodeDetailsRoute(
    guid: String,
    feedTitle: String,
    imageUrl: String,
    navigateUp: () -> Unit,
    viewModel: EpisodeDetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.getEpisode(guid)
    }
    EpisodeDetailsScreen(
        navigateUp = navigateUp,
        uiState = viewModel.uiState,
        feedTitle = feedTitle,
        imageUrl = imageUrl,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeDetailsScreen(
    navigateUp: () -> Unit,
    uiState: EpisodeDetailsUiState,
    feedTitle: String,
    imageUrl: String,
    modifier: Modifier = Modifier,
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    val episode = uiState.episodeViewData
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .placeholder(R.drawable.logo)
                            .crossfade(true)
                            .build(),
                        error = painterResource(id = R.drawable.logo),
                        contentDescription = null,
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = episode.title!!)
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = feedTitle)
                    Spacer(modifier = Modifier.height(5.dp))
                    Row {
//                        Text(text = DateUtils.dateToShortDate(episode.releaseDate!!))
                        Text(text = " - ")
                        Text(text = episode.duration!!)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(R.string.play))
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = HtmlUtils.htmlToSpannable(episode.description!!).toString())
                }
            }
        }
    }
}
