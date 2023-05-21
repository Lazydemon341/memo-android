package com.memo.chat.internal.ui.base

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.memo.chat.internal.ui.base.SymbolAnnotationType.LINK
import com.memo.chat.internal.ui.base.SymbolAnnotationType.PERSON
import com.memo.chat.internal.ui.model.MessageUiState
import com.memo.core.design.pull.refresh.PullRefreshIndicator
import com.memo.core.design.pull.refresh.pullRefresh
import com.memo.core.design.pull.refresh.rememberPullRefreshState
import com.memo.core.design.theme.MemoAppTheme
import com.memo.features.chat.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

private val JumpToBottomThreshold = 56.dp

private const val MessageContentType = "message"
private const val DayHeaderContentType = "day_header"

@Composable
internal fun Messages(
    pagingMessages: LazyPagingItems<MessageUiState>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val isRefreshing by remember(pagingMessages) {
        derivedStateOf {
            pagingMessages.loadState.refresh is LoadState.Loading ||
                pagingMessages.loadState.append is LoadState.Loading
        }
    }
    val pullRefreshState = rememberPullRefreshState(isRefreshing, pagingMessages::refresh)
    val scope = rememberCoroutineScope()
    val firstMessage = if (pagingMessages.itemCount > 0) pagingMessages[0] else null
    LaunchedEffect(firstMessage) {
        if (scrollState.firstVisibleItemIndex in 0..4) {
            scrollState.scrollToItem(0)
        }
    }
    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState),
    ) {
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            for (index in 0 until pagingMessages.itemCount) {
                val message = pagingMessages[index] ?: continue
                val prevMessage = if (index > 0) pagingMessages[index - 1] else null
                val nextMessage = if (index < pagingMessages.itemCount - 1)
                    pagingMessages[index + 1] else null
                val isFirstMessageByAuthor = prevMessage?.isByUser != message.isByUser
                val isLastMessageByAuthor = nextMessage?.isByUser != message.isByUser
                val isNewDay = nextMessage?.sentDate != message.sentDate

                item(
                    key = message.id,
                    contentType = MessageContentType,
                ) {
                    Message(
                        onAuthorClick = { name -> navigateToProfile(name) },
                        msg = message,
                        isUserMe = message.isByUser,
                        isNewDay = isNewDay,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }

                if (isNewDay) {
                    item(
                        contentType = DayHeaderContentType,
                    ) {
                        DayHeader(message.sentDate)
                    }
                }
            }
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                    scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        PullRefreshIndicator(
            modifier = Modifier
                .align(Alignment.TopCenter),
            refreshing = isRefreshing,
            state = pullRefreshState,
        )
    }
}

@Composable
private fun Message(
    onAuthorClick: (String) -> Unit,
    msg: MessageUiState,
    isUserMe: Boolean,
    isNewDay: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {
    val spaceBetweenAuthors = if (isLastMessageByAuthor) 8.dp else 0.dp
    Row(
        modifier = Modifier
            .padding(top = spaceBetweenAuthors)
            .padding(horizontal = 16.dp)
    ) {
        if (isLastMessageByAuthor && !isUserMe) {
            GlideImage(
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(end = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape),
                imageModel = { msg.authorImageUrl },
                previewPlaceholder = R.drawable.avatar_placeholder,
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                success = {
                    it.imageBitmap?.let { imageBitmap ->
                        Image(
                            modifier = Modifier,
                            bitmap = imageBitmap,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )
                    }
                },
                loading = {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(R.drawable.avatar_placeholder),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                },
                failure = {
                    Image(
                        modifier = Modifier,
                        painter = painterResource(R.drawable.avatar_placeholder),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                }
            )
        } else {
            Spacer(modifier = Modifier.width(58.dp))
        }
        AuthorAndTextMessage(
            modifier = Modifier
                .weight(1f),
            msg = msg,
            isUserMe = isUserMe,
            isNewDay = isNewDay,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = onAuthorClick,
        )
    }
}

@Composable
private fun AuthorAndTextMessage(
    msg: MessageUiState,
    isUserMe: Boolean,
    isNewDay: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor || isNewDay) {
            val horizontalAlignment = if (isUserMe) {
                Alignment.End
            } else {
                Alignment.Start
            }
            AuthorNameTimestamp(
                modifier = Modifier.align(horizontalAlignment),
                msg = msg,
            )
        }
        ChatItemBubble(msg, isUserMe, authorClicked = authorClicked)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(
    msg: MessageUiState,
    modifier: Modifier = Modifier
) {
    // Combine author and timestamp for a11y.
    Row(modifier = modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = "", // if (!isUserMe) msg.authorName else "",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp) // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = msg.sentTime,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

private val ReceivedMessageShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val SentMessageShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)

@Composable
private fun ChatItemBubble(
    message: MessageUiState,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {

    val backgroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val shape = if (isUserMe) {
        SentMessageShape
    } else {
        ReceivedMessageShape
    }
    val horizontalAlignment = if (isUserMe) {
        Alignment.End
    } else {
        Alignment.Start
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Surface(
            modifier = Modifier.align(horizontalAlignment),
            color = backgroundBubbleColor,
            shape = shape,
        ) {
            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
                authorClicked = authorClicked
            )
        }
    }
}

@Composable
private fun ClickableMessage(
    message: MessageUiState,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(
        text = message.content,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        LINK.name -> uriHandler.openUri(annotation.item)
                        PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

@Preview
@Composable
private fun DayHeaderPrev() {
    MemoAppTheme {
        DayHeader("Aug 6")
    }
}
