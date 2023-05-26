package com.podplay.android.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.podplay.android.R
import com.podplay.android.data.model.RecentSearch
import com.podplay.android.ui.common.RefreshIndicator

@Composable
fun SearchRoute(
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    SearchScreen(onPodcastClick, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun SearchScreen(
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    searchViewModel: SearchViewModel
) {

    val searchUiState = searchViewModel.searchUiState
    val recentSearchList = searchUiState.recentSearchFlow.collectAsState(listOf())

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyBoardController = LocalSoftwareKeyboardController.current

    val pullRefreshState = rememberPullRefreshState(
        refreshing = searchUiState.refreshing,
        onRefresh = { searchViewModel.searchPodcasts() }
    )

    Scaffold(topBar = {
        SearchBar(
            focusRequester = focusRequester,
            focusManager = focusManager,
            keyboardController = keyBoardController,
            viewModel = searchViewModel
        )
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
            ) {

                if (searchViewModel.searchQuery.isBlank()
                    && recentSearchList.value.isNotEmpty()
                    && searchUiState.isRecentSearchVisible
                ) {
                    RecentSearchComposable(recentSearchList.value, focusRequester, searchViewModel)
                }

                SearchResult(
                    podcastList = searchUiState.podcastSearchList,
                    pullRefreshState = pullRefreshState,
                    searchUiState = searchUiState,
                    onItemClick = onPodcastClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    viewModel: SearchViewModel,
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 10.dp, end = 10.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.isFocused) {
                    viewModel.updateShowSearchResult(false)
                }
            },
        value = viewModel.searchQuery,
        onValueChange = {
            viewModel.updateSearchQuery(it)
            if (viewModel.searchQuery.isBlank()) viewModel.updateRecentSearchVisibility(true)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        placeholder = {
            Text(text = stringResource(R.string.search))
        },
        trailingIcon = {
            if (viewModel.searchQuery.isNotBlank()) {
                IconButton(onClick = {
                    viewModel.clearSearch()
                    viewModel.updateRecentSearchVisibility(true)
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (viewModel.searchQuery.isNotBlank()) {
                    focusManager.clearFocus()
                    val recentSearch = RecentSearch(searchTerm = viewModel.searchQuery)
                    viewModel.saveSearchTerm(recentSearch)
                    viewModel.searchPodcasts()
                }
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun RecentSearchComposable(
    recentSearchList: List<RecentSearch>,
    focusRequester: FocusRequester,
    viewModel: SearchViewModel,
) {
    val interactionSource = remember { MutableInteractionSource() }
    Text(
        text = stringResource(id = R.string.recent),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 17.dp, bottom = 13.dp)
    )
    LazyColumn {
        items(recentSearchList) { recentSearch ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = recentSearch.searchTerm,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                        ) {
                            focusRequester.requestFocus()
                            viewModel.updateSearchQuery(recentSearch.searchTerm)
                        }
                )
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = stringResource(
                        R.string.clear_from_history,
                        recentSearch.searchTerm
                    ),
                    modifier = Modifier.clickable {
                        viewModel.deleteSearchTerm(recentSearch.searchTerm)
                    }
                )
            }
        }
    }
    Text(
        text = stringResource(id = R.string.clear_search_history),
        style = MaterialTheme.typography.bodyMedium,
        fontSize = 15.sp,
        modifier = Modifier
            .padding(top = 10.dp)
            .clickable {
                viewModel.clearSearchHistory()
            }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchResult(
    podcastList: List<SearchViewModel.PodcastSummaryViewData>,
    pullRefreshState: PullRefreshState,
    searchUiState: SearchUiState,
    onItemClick: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .pullRefresh(pullRefreshState)
    ) {
        if (searchUiState.showSearchResults) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (!searchUiState.refreshing) {
                    items(podcastList) { podcast ->
                        PodcastItem(podcast = podcast, onItemClicked = onItemClick)
                    }
                }
            }
        }
        RefreshIndicator(
            refreshing = searchUiState.refreshing,
            pullRefreshState = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastItem(
    podcast: SearchViewModel.PodcastSummaryViewData,
    onItemClicked: (feedUrl: String, imageUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(podcast.imageUrl)
                    .size(56)
                    .placeholder(R.drawable.logo)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(4.dp))
            )
        },
        headlineText = {
            Text(
                text = podcast.name!!,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        modifier = modifier.clickable {
            onItemClicked(
                podcast.feedUrl!!,
                podcast.imageUrl!!
            )
        }
    )
}

