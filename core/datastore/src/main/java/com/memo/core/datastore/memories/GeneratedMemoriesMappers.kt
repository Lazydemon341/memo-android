package com.memo.core.datastore.memories

import com.memo.core.model.Location
import com.memo.core.model.memories.GeneratedMemory
import com.memo.core.model.memories.MemoryPhoto
import com.memo.core.datastore.memories.Location as PersistedLocation
import com.memo.core.datastore.memories.MemoryPhoto as PersistedMemoryPhoto

fun GeneratedMemory.toPersistableModel(): Memory {
    return Memory(
        local_id = memoryId,
        title = title,
        caption = caption,
        memory_photos = photos.map { it.toPersistableModel() }
    )
}

fun Memory.toExternalModel(): GeneratedMemory {
    return GeneratedMemory(
        memoryId = local_id,
        title = title,
        caption = caption,
        photos = memory_photos.map { it.toExternalModel() },
    )
}

private fun MemoryPhoto.toPersistableModel(): PersistedMemoryPhoto {
    return PersistedMemoryPhoto(
        photo_uri = this.photoUri,
        timestamp = this.timestamp,
        location = this.photoLocation?.toPersistableModel(),
        tags = this.tags,
    )
}

private fun Location.toPersistableModel(): PersistedLocation {
    return PersistedLocation(
        latitude = latitude,
        longitude = longitude,
    )
}

private fun PersistedMemoryPhoto.toExternalModel(): MemoryPhoto {
    return MemoryPhoto(
        photoUri = photo_uri,
        timestamp = timestamp,
        photoLocation = location?.toExternalModel(),
        tags = tags,
    )
}

private fun PersistedLocation.toExternalModel(): Location {
    return Location(
        latitude = latitude,
        longitude = longitude,
    )
}
