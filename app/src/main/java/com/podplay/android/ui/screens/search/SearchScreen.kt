package com.podplay.android.ui.screens.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.podplay.android.ui.compose.PodcastList

@Composable
fun SearchRoute(
    onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    SearchScreen(onPodcastClick, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(onPodcastClick: (feedUrl: String, imageUrl: String) -> Unit, searchViewModel: SearchViewModel) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val keyBoardController = LocalSoftwareKeyboardController.current


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
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                val searchUiState = searchViewModel.searchUiState
                if (searchUiState.isSearching) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (searchUiState.podcastSearchList.isNotEmpty()) {
                    PodcastList(
                        podcastList = searchUiState.podcastSearchList,
                        onItemClick = onPodcastClick,
                    )
                }
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
            .focusRequester(focusRequester),
        value = viewModel.searchQuery,
        onValueChange = viewModel::updateSearchQuery,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            if (viewModel.searchQuery.isNotBlank()) {
                IconButton(onClick = {
                    viewModel.clearSearch()
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
