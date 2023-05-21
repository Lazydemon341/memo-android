package com.memo.demo.app.photo.meta.inf

interface ReportWriter {
    /**
     * Создает репорт в виде
     * [12, 34.5, 34.5; 13, 35, 35;] - memory 1
     * [12, 34.5, 34.5] - memory 2
     * {12, 34.5, 34.5; 12, 34.5, 34.5; 12, 34.5, 34.5 } -- all photos report
     */
    fun getReport(memoriesReports: List<String>, allPhotosReport: String): String

    fun getAllPhotosReport(photosData: List<String>): String

    fun getMemoryReport(photosData: List<String>): String

    fun getImageReport(date: String, latLonArray: DoubleArray): String
}
