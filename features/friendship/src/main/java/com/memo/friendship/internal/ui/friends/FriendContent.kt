package com.memo.friendship.internal.ui.friends

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import com.memo.core.model.friendship.Friend
import com.memo.features.friendship.R.string

@Composable
internal fun FriendContent(
    friend: Friend,
    onOpenFriendProfile: (Long) -> Unit,
    onDeleteFriend: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    var openDialog by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .clickable { openDialog = true },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray),
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = friend.name,
            color = Color.White,
        )
    }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = {
                Column {
                    Text(text = friend.name)
                }
            },
            confirmButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(id = string.show_profile_button)),
                    style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    onOpenFriendProfile(friend.id)
                    openDialog = false
                }
            },
            dismissButton = {
                ClickableText(
                    text = AnnotatedString(stringResource(id = string.delete_friend_button)),
                    style = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onPrimary),
                ) {
                    onDeleteFriend(friend.id)
                    openDialog = false
                }
            }
        )
    }
}
