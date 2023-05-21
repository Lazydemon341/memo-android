package com.memo.core.datastore.memories

import androidx.datastore.core.Serializer
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class MemoriesSerializer @Inject constructor() : Serializer<Memories> {

    private val adapter = Memories.ADAPTER

    override val defaultValue = Memories()

    override suspend fun readFrom(input: InputStream): Memories {
        return adapter.decode(input)
    }

    override suspend fun writeTo(t: Memories, output: OutputStream) {
        adapter.encode(output, t)
    }
}
