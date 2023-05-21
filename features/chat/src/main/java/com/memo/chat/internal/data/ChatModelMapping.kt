package com.memo.chat.internal.data

import com.memo.chat.internal.domain.model.MessageToSend
import com.memo.core.network.model.messaging.SendMessageParams

internal fun MessageToSend.toNetworkModel(): SendMessageParams {
    return SendMessageParams(
        text = text,
        chatId = chatId,
    )
}
