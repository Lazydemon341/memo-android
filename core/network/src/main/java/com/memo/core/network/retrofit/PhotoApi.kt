package com.memo.core.network.retrofit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.memo.core.network.NetworkPhotoDataSource
import com.memo.core.network.model.photo.UploadPhotoResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

interface PhotoApi {
    @Multipart
    @POST("photo/upload")
    suspend fun upload(
        @Part photo: MultipartBody.Part,
    ): UploadPhotoResponse

    @GET("photo/get")
    suspend fun get(@Query("id") id: Long): ResponseBody
}

@Singleton
class RetrofitPhotoDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkPhotoDataSource {

    private val api = apiFactory.create(PhotoApi::class.java)

    override suspend fun uploadPhoto(photoFile: File): UploadPhotoResponse {
        return api.upload(
            photo = MultipartBody.Part.createFormData(
                name = "photos",
                filename = photoFile.name,
                body = photoFile.asRequestBody("image/*".toMediaTypeOrNull())
            ),
        )
    }

    override suspend fun getPhoto(id: Long): Bitmap {
        val response = api.get(id)
        return response.byteStream().use {
            BitmapFactory.decodeStream(it)
        }
    }
}
