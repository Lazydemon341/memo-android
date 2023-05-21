package com.memo.demo.app.photo.meta.inf

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.exifinterface.media.ExifInterface

class PhotosMetaDataReporterImpl(
    private val reportWriter: ReportWriter
) : PhotosMetaDataReporter {
    private val isAtLeastQ = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    private val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        )
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    @WorkerThread
    override fun getSelectedImagesMetaInfo(context: Context, uris: List<Uri>): List<String> {
        val result = arrayListOf<String>()

        uris.forEach { uri ->

            var date = ""
            var latLonArray = DoubleArray(2)

            context.applicationContext.contentResolver.openInputStream(uri)?.use { stream ->
                val exifInterface = ExifInterface(stream)
                exifInterface.latLong?.let { latLonArray = it }
                date = (exifInterface.getImageDateTime()).toString()
            }
            result.add(reportWriter.getImageReport(date, latLonArray))
        }

        return result
    }

    @WorkerThread
    override fun getAllImagesInfo(context: Context): List<String> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_ADDED
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} ASC"
        val result = arrayListOf<String>()

        context.applicationContext.contentResolver.query(
            collection, projection, null, null, sortOrder
        )?.use { cursor ->

            val idColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val dateTakenColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val latColumn =
                if (isAtLeastQ) -1 else cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE)
            val longColumn =
                if (isAtLeastQ) -1 else cursor.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE)

            while (cursor.moveToNext()) {

                var date = cursor.getString(dateTakenColumn)
                val photoId = cursor.getString(idColumn)

                val latLonRequiredFromExif = latColumn == -1 || longColumn == -1
                val dateTimeRequiredFromExif = date == null

                var latLonArray = DoubleArray(2)

                if (latLonRequiredFromExif || dateTimeRequiredFromExif) {
                    var photoUri: Uri = Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, photoId
                    )
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        photoUri = MediaStore.setRequireOriginal(photoUri)
                    }
                    context.applicationContext.contentResolver.openInputStream(photoUri)
                        ?.use { stream ->
                            val exifInterface = ExifInterface(stream)
                            if (latLonRequiredFromExif) {
                                exifInterface.latLong?.let { latLonArray = it }
                            }
                            if (dateTimeRequiredFromExif) {
                                date = (exifInterface.getImageDateTime()).toString()
                            }
                        }
                } else {
                    latLonArray = doubleArrayOf(
                        cursor.getFloat(latColumn).toDouble(),
                        cursor.getFloat(longColumn).toDouble()
                    )
                }
                result.add(reportWriter.getImageReport(date, latLonArray))
            }
        }
        return result
    }

    @SuppressLint("RestrictedApi")
    private fun ExifInterface.getImageDateTime(): Long {
        return this.gpsDateTime ?: this.dateTime ?: 0L
    }
}
