package com.memo.chats.list.internal.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.chats.list.internal.ui.model.ChatUiState
import com.memo.chats.list.internal.ui.model.LastMessageUiState
import com.memo.core.design.theme.MemoAppTheme
import com.memo.features.chats_list.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlin.random.Random

@Composable
internal fun Chat(
    chat: ChatUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        GlideImage(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(48.dp)
                .clip(CircleShape),
            previewPlaceholder = R.drawable.avatar_placeholder,
            imageModel = { chat.photoUrl },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            loading = {
                Image(
                    painter = painterResource(R.drawable.avatar_placeholder),
                    contentDescription = null,
                )
            },
            failure = {
                Image(
                    painter = painterResource(R.drawable.avatar_placeholder),
                    contentDescription = null,
                )
            },
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            ChatTitleWithLastSentTime(
                modifier = Modifier.fillMaxWidth(),
                title = chat.title,
                lastSentTime = chat.lastMessage.sentTime,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = chat.lastMessage.content,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun ChatTitleWithLastSentTime(
    title: String,
    lastSentTime: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = lastSentTime,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )
    }
}

@Preview
@Composable
private fun ChatPreview() {
    MemoAppTheme {
        Chat(chat = previewChat)
    }
}

private val previewChat = ChatUiState(
    title = "Бульдозер Саня",
    id = Random.nextLong(),
    photoUrl = "https://www.linkpicture.com/q/2022-08-25-22.22.43.jpg",
    lastMessage = LastMessageUiState(
        content = "Привет! Выложил пр ленты. Посмотри, пожалуйста, как будет время.",
        sentTime = "4:20",
    ),
    interlocutorEmail = "",
)
