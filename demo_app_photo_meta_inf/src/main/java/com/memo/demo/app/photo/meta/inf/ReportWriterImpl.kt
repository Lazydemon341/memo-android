package com.memo.demo.app.photo.meta.inf

class ReportWriterImpl : ReportWriter {

    override fun getReport(memoriesReports: List<String>, allPhotosReport: String): String {
        return memoriesReports.joinToString(separator = MEMORIES_SEPARATOR, postfix = MEMORIES_SEPARATOR) + allPhotosReport
    }

    override fun getAllPhotosReport(photosData: List<String>): String {
        return photosData.joinToString(separator = PHOTOS_SEPARATOR, prefix = ALL_PHOTOS_PREFIX, postfix = ALL_PHOTOS_POSTFIX)
    }

    override fun getMemoryReport(photosData: List<String>): String {
        return photosData.joinToString(separator = PHOTOS_SEPARATOR, prefix = MEMORY_PREFIX, postfix = MEMORY_POSTFIX)
    }

    override fun getImageReport(date: String, latLonArray: DoubleArray): String {
        return "$date$META_DATA_VALUES_SEPARATOR" +
            "${latLonArray[0]}$META_DATA_VALUES_SEPARATOR" +
            "${latLonArray[1]}"
    }

    private companion object {
        private const val META_DATA_VALUES_SEPARATOR = ","
        private const val PHOTOS_SEPARATOR = ";"
        private const val MEMORIES_SEPARATOR = "\n"
        private const val ALL_PHOTOS_PREFIX = "{"
        private const val ALL_PHOTOS_POSTFIX = "}"
        private const val MEMORY_PREFIX = "["
        private const val MEMORY_POSTFIX = "]"
    }
}
