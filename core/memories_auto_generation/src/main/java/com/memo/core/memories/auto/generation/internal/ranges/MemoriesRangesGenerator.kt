package com.memo.core.memories.auto.generation.internal.ranges

import com.memo.core.memories.auto.generation.api.models.MemoryRequestParams
import com.memo.core.memories.auto.generation.internal.images.ImageMetaData
import javax.inject.Inject

internal class MemoriesRangesGenerator @Inject constructor() {

    fun concatImagesIntoMemoryRanges(images: List<ImageMetaData>, params: MemoryRequestParams): List<MemoryRange> {
        val batches = getBatchesOfImagesByDay(images)
        val memoryRanges = concatDayBatchesIntoMemoryRanges(batches, params.maxDaysBetweenMemorableDays)
        val filteredMemoryRanges = removeNotValidMemoryRanges(memoryRanges, params.minPhotosInMemory)
        return filteredMemoryRanges
    }

    private fun getBatchesOfImagesByDay(images: List<ImageMetaData>): List<DayBatchOfImages> {
        val batches = putImagesIntoBatches(images)
        return filterBatches(batches)
    }

    private fun putImagesIntoBatches(images: List<ImageMetaData>): List<DayBatchOfImages> {
        val daysBatchesMap = HashMap<Int, MutableList<ImageMetaData>>()

        images.forEach { metaData ->
            if (metaData.dateTimeMillis != null) {
                val dayTaken = metaData.dateTimeMillis.let(::convertMillisToDayNum)
                val entry = daysBatchesMap[dayTaken]
                if (entry == null) {
                    val newList = mutableListOf(metaData)
                    daysBatchesMap[dayTaken] = newList
                } else {
                    entry.add(metaData)
                }
            }
        }
        return daysBatchesMap.entries.sortedByDescending { it.key }.map {
            DayBatchOfImages(
                it.key.toLong(),
                it.value
            )
        }
    }

    private fun filterBatches(batches: List<DayBatchOfImages>): List<DayBatchOfImages> {
        val average = batches.sumOf { it.imagesCount } / batches.size.toFloat()
        return batches.filter { isBatchSuitable(it, average) }
    }

    private fun isBatchSuitable(batch: DayBatchOfImages, averageNumOfImagesPerBatch: Float): Boolean {
        return batch.imagesCount > averageNumOfImagesPerBatch
    }

    private fun concatDayBatchesIntoMemoryRanges(batches: List<DayBatchOfImages>, maxDaysBetweenMemorableDays: Int): List<MemoryRange> {
        var key = 0
        var previousDay = 0L
        return batches.sortedByDescending { it.dayNumInUnixTime }.groupBy { batch ->
            if (previousDay - batch.dayNumInUnixTime > maxDaysBetweenMemorableDays) {
                key += 1
            }
            previousDay = batch.dayNumInUnixTime
            key
        }.values.map { batchesInMemory ->
            MemoryRange(
                batchesInMemory.flatMap { singleBatch -> singleBatch.images }
            )
        }
    }

    private fun removeNotValidMemoryRanges(memories: List<MemoryRange>, minImagesInMemory: Int): List<MemoryRange> {
        return memories.filter {
            it.imagesCount >= minImagesInMemory
        }
    }

    private class DayBatchOfImages(
        val dayNumInUnixTime: Long,
        val images: List<ImageMetaData>,
    ) {
        val imagesCount = images.size

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DayBatchOfImages

            if (dayNumInUnixTime != other.dayNumInUnixTime) return false

            return true
        }

        override fun hashCode(): Int {
            return dayNumInUnixTime.hashCode()
        }
    }

    private companion object {
        private fun convertMillisToDayNum(timestampMillis: Long): Int {
            return (timestampMillis / 1000 / 60 / 60 / 24).toInt()
        }
    }
}
