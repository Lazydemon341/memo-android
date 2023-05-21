package com.memo.friendship.internal.ui.requests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.memo.core.design.text.MemoCommentText
import com.memo.core.design.theme.MemoAppTheme
import com.memo.core.model.friendship.FriendshipRequest
import com.memo.core.model.friendship.FriendshipRequestUser
import com.memo.features.friendship.R
import com.memo.features.friendship.R.string

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RequestContent(
    request: FriendshipRequest,
    onAccept: (Long) -> Unit,
    onDecline: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openDialog by remember { mutableStateOf(false) }
    Card(
        modifier = modifier,
        onClick = {
            openDialog = true
        },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = request.user.name,
            color = Color.White,
        )
    }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Column {
                    Text(text = request.user.name)
                    MemoCommentText(text = stringResource(string.friendship_request_text))
                }
            },
            confirmButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.accept_request)),
                    style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    onAccept(request.id)
                    openDialog = false
                }
            },
            dismissButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(id = R.string.decline_request)),
                    style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    onDecline(request.id)
                    openDialog = false
                }
            }
        )
    }
}

@Composable
@Preview
private fun RequestContentPreview() {
    MemoAppTheme {
        RequestContent(
            request = FriendshipRequest(
                id = 0,
                user = FriendshipRequestUser(id = 0, name = "name")
            ),
            onAccept = {},
            onDecline = {},
        )
    }
}
