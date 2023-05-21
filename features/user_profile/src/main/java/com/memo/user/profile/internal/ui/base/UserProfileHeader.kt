package com.memo.user.profile.internal.ui.base

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.request.RequestOptions
import com.memo.features.user_profile.R
import com.memo.user.profile.internal.ui.model.UserProfileUiState
import com.skydoves.landscapist.glide.GlideImage

private const val minAvatarSize = 32
private const val maxAvatarSize = 80

@Composable
internal fun CollapsedHeaderContent(
    collapsedFraction: Float,
    onBackClick: () -> Unit,
    onTitleClick: () -> Unit,
    onMoreClick: () -> Unit,
    profileUiStateProvider: () -> UserProfileUiState,
    modifier: Modifier = Modifier,
) {
    val name = (profileUiStateProvider() as? UserProfileUiState.Ready)?.name.orEmpty()
    Box(
        modifier = modifier,
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable { onBackClick() },
            imageVector = Filled.ArrowBack,
            tint = Color.White,
            contentDescription = "",
        )
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .clickable {
                    onTitleClick()
                },
            text = name,
            color = Color.White.copy(alpha = collapsedFraction),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .clickable {
                    onMoreClick()
                },
            imageVector = Filled.Menu,
            tint = Color.White,
            contentDescription = "",
        )
    }
}

@Composable
internal fun ExtendedHeaderContent(
    extendedFraction: Float,
    profileUiStateProvider: () -> UserProfileUiState,
    onAvatarChanged: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = (profileUiStateProvider() as? UserProfileUiState.Ready)?.name.orEmpty()
    val avatarUrl = (profileUiStateProvider() as? UserProfileUiState.Ready)?.avatarUrl.orEmpty()
    Box(
        modifier = modifier,
    ) {
        Image(
            modifier = Modifier
                .matchParentSize(),
            painter = painterResource(id = R.drawable.profile_background),
            contentScale = ContentScale.FillBounds,
            alpha = extendedFraction,
            contentDescription = "",
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface,
                        )
                    )
                ),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(64.dp))
            UserAvatar(
                extendedFraction = extendedFraction,
                avatarUrl = avatarUrl,
                onAvatarChanged = onAvatarChanged,
            )
            Spacer(modifier = Modifier.height(8.dp))
            UserFullName(
                name = name,
            )
            Spacer(modifier = Modifier.height(8.dp))
//            UserDescription(
//                description = "",
//            )
//            Spacer(modifier = Modifier.height(8.dp))
            NumbersRow(
                profileUiStateProvider = profileUiStateProvider,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun UserAvatar(
    extendedFraction: Float,
    avatarUrl: String,
    onAvatarChanged: (Uri) -> Unit,
    modifier: Modifier = Modifier,
) {
    val size = (minAvatarSize + (maxAvatarSize - minAvatarSize) * extendedFraction).dp
    val imageModifier = Modifier
        .size(size)
        .clip(CircleShape)
    val launcher = rememberLauncherForActivityResult(
        contract = PickVisualMedia(),
        onResult = { uri ->
            uri?.let { onAvatarChanged(it) }
        },
    )
    val request = remember { PickVisualMediaRequest(ImageOnly) }
    GlideImage(
        modifier = modifier
            .size(size)
            .clickable { launcher.launch(request) },
        imageModel = { avatarUrl },
        requestOptions = { RequestOptions().override(maxAvatarSize) },
        success = {
            Image(
                modifier = imageModifier,
                bitmap = checkNotNull(it.imageBitmap),
                contentDescription = null,
            )
        },
        loading = {
            Image(
                modifier = imageModifier,
                painter = painterResource(R.drawable.profile_avatar),
                contentDescription = null,
            )
        },
        failure = {
            Image(
                modifier = imageModifier,
                painter = painterResource(R.drawable.profile_avatar),
                contentDescription = null,
            )
        },
    )
}

@Composable
private fun UserFullName(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = name,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 18.sp,
    )
}

@Composable
private fun UserDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = description,
        textAlign = TextAlign.Center,
        color = Color.White,
        fontSize = 12.sp,
    )
}

@Composable
private fun NumbersRow(
    profileUiStateProvider: () -> UserProfileUiState,
    modifier: Modifier = Modifier,
) {
    val friendsCount = (profileUiStateProvider() as? UserProfileUiState.Ready)?.friendsCount ?: 0
    val memoriesCount = (profileUiStateProvider() as? UserProfileUiState.Ready)?.memories?.size ?: 0
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        NumberWithSubtitle(
            number = friendsCount,
            subtitle = stringResource(R.string.friends_count_title),
        )
        NumberWithSubtitle(
            number = memoriesCount,
            subtitle = stringResource(R.string.memories_count_title),
        )
    }
}

@Composable
private fun NumberWithSubtitle(
    number: Int,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$number",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        Text(
            text = subtitle,
            color = Color.White,
            fontSize = 12.sp,
        )
    }
}
