package com.memo.publish_photo.internal.ui

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.memo.core.design.text.MemoCommentText
import com.memo.core.design.text.MemoInputText
import com.memo.core.design.text.MemoMainText
import com.memo.features.publish_photo.R
import com.memo.publish_photo.internal.model.FriendUiState
import com.memo.publish_photo.internal.model.LocationUiState
import com.memo.publish_photo.internal.model.PhotoUploadingUiState
import com.memo.publish_photo.internal.model.PublishPhotoUiState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
internal fun PublishPhotoRoute(
    onBackPressed: () -> Unit,
    viewModel: PublishPhotoViewModel = hiltViewModel(),
) {
    val publishPhotoUiState = viewModel.publishPhotoUiState.collectAsStateWithLifecycle()
    val uploadingUiState = viewModel.uploadingUiState.collectAsStateWithLifecycle()
    PublishPhotoScreen(
        state = publishPhotoUiState,
        uploadingStateProvider = uploadingUiState::value,
        onFriendClick = viewModel::onFriendClick,
        onBackPressed = onBackPressed,
        onPublishPhoto = viewModel::publishPhoto,
        onDownloadPhoto = viewModel::downloadPhoto,
    )
}

@Composable
private fun PublishPhotoScreen(
    state: State<PublishPhotoUiState>,
    uploadingStateProvider: () -> PhotoUploadingUiState,
    onFriendClick: (Long, Boolean) -> Unit,
    onBackPressed: () -> Unit,
    onPublishPhoto: () -> Unit,
    onDownloadPhoto: () -> Unit,
) {
    val uploadingState = uploadingStateProvider()
    val context = LocalContext.current
    LaunchedEffect(uploadingState) {
        if (uploadingStateProvider() is PhotoUploadingUiState.Success) {
            Toast.makeText(context, "Post uploaded!", Toast.LENGTH_SHORT).show()
        }
    }
    val publishPhotoUiState = state.value
    if (publishPhotoUiState !is PublishPhotoUiState.Ready) {
        return
    }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        val (friends, photoWithComment, location, actions) = createRefs()
        Friends(
            friendsUiState = publishPhotoUiState.friends,
            onFriendClick = onFriendClick,
            modifier = Modifier.constrainAs(friends) {
                bottom.linkTo(photoWithComment.top, margin = 16.dp)
            }
        )
        PhotoWithComment(
            modifier = Modifier.constrainAs(photoWithComment) {
                top.linkTo(parent.top, margin = 172.dp)
            },
            photo = publishPhotoUiState.photo,
        )
        Location(
            modifier = Modifier.constrainAs(location) {
                top.linkTo(photoWithComment.bottom, margin = 16.dp)
            },
            locationUiState = publishPhotoUiState.locationUiState,
        )
        Actions(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(actions) {
                    top.linkTo(photoWithComment.bottom)
                    bottom.linkTo(parent.bottom)
                },
            onClose = onBackPressed,
            onPublish = onPublishPhoto,
            onDownload = onDownloadPhoto,
        )
    }
}

@Composable
private fun Friends(
    friendsUiState: List<FriendUiState>,
    onFriendClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (friendsUiState.isEmpty()) {
        return
    }
    Column(modifier = modifier) {
        MemoMainText(
            text = "Send to:",
        )
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            items(
                items = friendsUiState,
                key = { it.id },
            ) {
                Friend(
                    modifier = Modifier.width(48.dp),
                    friendUiState = it,
                    onFriendClick = onFriendClick,
                )
            }
        }
    }
}

@Composable
private fun Friend(
    friendUiState: FriendUiState,
    onFriendClick: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderWidth by animateDpAsState(if (friendUiState.isSelected) 2.dp else 0.dp)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IconButton(
            modifier = Modifier.then(Modifier.size(36.dp)),
            onClick = { onFriendClick(friendUiState.id, !friendUiState.isSelected) }
        ) {
            GlideImage(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(12.dp),
                    )
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = borderWidth,
                        color = Color.Yellow,
                        shape = RoundedCornerShape(12.dp),
                    ),
                imageModel = { friendUiState.imageUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                ),
                loading = { _ ->
                    Image(
                        painter = painterResource(id = R.drawable.avatar_placeholder),
                        contentDescription = "",
                    )
                },
                failure = { state ->
                    Image(
                        painter = painterResource(id = R.drawable.avatar_placeholder),
                        contentDescription = "",
                    )
                }
            )
        }
        Text(
            text = friendUiState.name,
            color = Color.White,
            fontSize = 12.sp,
            maxLines = 1,
        )
    }
}

@Composable
private fun PhotoWithComment(
    photo: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(48.dp)),
            contentScale = ContentScale.Crop,
            bitmap = photo,
            contentDescription = "",
        )
        MemoInputText(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter),
            placeholderText = stringResource(R.string.publish_photo_comment_placeholder),
        )
    }
}

@Composable
private fun Location(
    locationUiState: LocationUiState,
    modifier: Modifier = Modifier,
) {
    if (locationUiState is LocationUiState.Enabled) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Filled.LocationOn,
                contentDescription = "",
            )
            Column(
                modifier = Modifier.padding(start = 4.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = locationUiState.location,
                    maxLines = 1,
                    fontSize = 16.sp,
                )
                MemoCommentText(
                    modifier = Modifier.fillMaxWidth(),
                    text = "tap to disable",
                )
            }
        }
    }
}

@Composable
private fun Actions(
    onClose: () -> Unit,
    onPublish: () -> Unit,
    onDownload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onClose,
            content = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Filled.Close,
                    contentDescription = "",
                )
            },
        )
        Button(
            modifier = Modifier
                .size(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
            ),
            shape = CircleShape,
            content = {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Filled.Check,
                    tint = Color.Black,
                    contentDescription = "",
                )
            },
            onClick = onPublish,
        )
        IconButton(
            onClick = onDownload,
            content = {
                Icon(
                    modifier = Modifier.size(48.dp),
                    imageVector = Filled.Download,
                    contentDescription = "",
                )
            },
        )
    }
}
