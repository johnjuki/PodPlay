package com.podplay.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeRoute(onSearchBarClick: () -> Unit) {
    HomeScreen(onSearchBarClick = onSearchBarClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onSearchBarClick: () -> Unit, modifier: Modifier = Modifier,) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "PodPlay") }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                SearchIcon {
                    onSearchBarClick()
                }
            }
        }

    }
}

@Composable
private fun SearchIcon(onSearchBarClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        IconButton(onClick = onSearchBarClick) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        }
    }
}
