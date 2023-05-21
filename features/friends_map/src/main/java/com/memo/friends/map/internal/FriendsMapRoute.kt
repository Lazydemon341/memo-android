package com.memo.friends.map.internal

import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.icons.MemoIcons
import com.memo.core.model.map.NavigateToPostArguments
import com.memo.features.friends.map.R
import com.memo.friends.map.internal.FilterType.FOR_TODAY
import com.memo.friends.map.internal.FilterType.FOR_WEEK
import com.memo.friends.map.internal.FilterType.FOR_YESTERDAY
import com.memo.friends.map.internal.ui.Map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FriendsMapRoute(
    onBackPressed: () -> Unit,
    navigateToPost: (NavigateToPostArguments) -> Unit,
    viewModel: FriendsMapViewModel = hiltViewModel()
) {
    val uiState = viewModel.mapUiState.collectAsStateWithLifecycle()
    val selectedChip = viewModel.filterTypeFlow.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.requestPhotos()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Map(
            mapUiState = uiState.value,
            onPhotoClicked = { navigateToPost(NavigateToPostArguments(it)) }
        )
        IconButton(
            onClick = onBackPressed,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                MemoIcons.ArrowBack, contentDescription = "", tint = Color.Black
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .height(96.dp)
                .scrollable(rememberScrollState(), Horizontal)
                .padding(bottom = 24.dp, start = 16.dp, end = 16.dp)
                .align(Alignment.BottomStart)
        ) {
            ElevatedFilterChip(
                selected = selectedChip.value == FilterType.FOR_TODAY,
                onClick = { viewModel.setFilteringType(FOR_TODAY) },
                colors = chipColors(),
                label = {
                    Text(stringResource(R.string.for_today_filter_title))
                }
            )
            ElevatedFilterChip(
                selected = selectedChip.value == FilterType.FOR_YESTERDAY,
                onClick = { viewModel.setFilteringType(FOR_YESTERDAY) },
                colors = chipColors(),
                label = {
                    Text(stringResource(R.string.for_yesterday_filter_title))
                }
            )
            ElevatedFilterChip(
                selected = selectedChip.value == FilterType.FOR_WEEK,
                onClick = { viewModel.setFilteringType(FOR_WEEK) },
                colors = chipColors(),
                label = {
                    Text(stringResource(R.string.for_the_week_filter_title))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun chipColors() = FilterChipDefaults.elevatedFilterChipColors(
    selectedContainerColor = Color.White,
    selectedLabelColor = Color.Black,
)
