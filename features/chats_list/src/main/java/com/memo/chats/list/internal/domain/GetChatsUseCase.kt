package com.memo.chats.list.internal.domain

import com.memo.chats.list.api.domain.Chat
import com.memo.chats.list.api.domain.ChatsListRepository
import javax.inject.Inject

internal class GetChatsUseCase @Inject constructor(
    private val chatsListRepository: ChatsListRepository,
) {

    suspend operator fun invoke(): Result<List<Chat>> {
        return chatsListRepository.getChats()
    }
}
