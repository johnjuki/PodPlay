package com.podplay.android.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.podplay.android.R

@Composable
fun HomeRoute(onSearchBarClick: () -> Unit) {
    HomeScreen(onSearchBarClick = onSearchBarClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(onSearchBarClick: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "") }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(R.drawable.people_recording),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = stringResource(R.string.app_description))
                Spacer(modifier = Modifier.height(20.dp))
                SearchBar(onSearchBarClick = onSearchBarClick)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(onSearchBarClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 10.dp, end = 10.dp)
                .clickable { onSearchBarClick() },
            value = "",
            enabled = false,
            onValueChange = { },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            },
            shape = RoundedCornerShape(5.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
}
