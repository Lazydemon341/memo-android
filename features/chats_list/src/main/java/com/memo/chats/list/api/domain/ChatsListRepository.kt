package com.memo.chats.list.api.domain

interface ChatsListRepository {
    suspend fun getChats(): Result<List<Chat>>
}
