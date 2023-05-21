package com.memo.core.datastore.friendship

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class FriendshipSerializer @Inject constructor() : Serializer<Friendship> {

    private val adapter = Friendship.ADAPTER

    override val defaultValue = Friendship()

    override suspend fun readFrom(input: InputStream): Friendship {
        return adapter.decode(input)
    }

    override suspend fun writeTo(t: Friendship, output: OutputStream) {
        adapter.encode(output, t)
    }
}
