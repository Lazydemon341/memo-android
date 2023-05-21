package com.memo.chat.internal.domain

import com.memo.chat.internal.data.ChatRepository
import com.memo.chat.internal.domain.model.MessageToSend
import javax.inject.Inject

internal class SendMessageUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
) {
    suspend operator fun invoke(message: MessageToSend): Result<Unit> {
        return chatRepository.sendMessage(message = message)
    }
}
