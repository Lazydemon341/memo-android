package com.memo.memories.internal.ui.sections

private const val usersSectionContentType = "users_section"

// internal fun LazyListScope.UsersSection(usersUiState: UsersUiState) {
//    if (usersUiState.shouldShowUsers) {
//        item(
//            contentType = usersSectionContentType,
//        ) {
//            Row(
//                modifier = Modifier.padding(top = 16.dp),
//                horizontalArrangement = Arrangement.spacedBy((-6).dp),
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                usersUiState.users.forEach {
//                    User(it)
//                }
//                if (usersUiState.shouldShowDescription) {
//                    MemoCommentText(
//                        modifier = Modifier.padding(start = 20.dp),
//                        text = usersUiState.description
//                    )
//                }
//            }
//        }
//    }
// }
//
// @Composable
// private fun User(user: UserUiState) {
//    Image(
//        modifier = Modifier
//            .size(42.dp)
//            .clip(CircleShape)
//            .border(width = 2.dp, color = Color.Black, shape = CircleShape),
//        painter = painterResource(drawable.user_avatar_test),
//        contentDescription = ""
//    )
// }
