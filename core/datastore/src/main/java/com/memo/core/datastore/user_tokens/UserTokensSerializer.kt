package com.memo.core.datastore.user_tokens

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserTokensSerializer @Inject constructor() : Serializer<UserTokens> {

    private val adapter = UserTokens.ADAPTER

    override val defaultValue = UserTokens()

    override suspend fun readFrom(input: InputStream): UserTokens {
        return adapter.decode(input)
    }

    override suspend fun writeTo(t: UserTokens, output: OutputStream) {
        adapter.encode(output, t)
    }
}
