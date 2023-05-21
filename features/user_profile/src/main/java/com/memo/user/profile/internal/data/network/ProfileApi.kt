package com.memo.user.profile.internal.data.network

import com.memo.core.network.retrofit.ApiFactory
import com.memo.user.profile.internal.data.network.model.ProfileDataResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

internal interface ProfileApi {

    @GET("profile/data")
    suspend fun getData(@Query("user_id") userId: Long?): ProfileDataResponse

    @Multipart
    @POST("profile/change_avatar")
    suspend fun changeAvatar(@Part photo: MultipartBody.Part)
}

@Singleton
internal class NetworkProfileDataSource @Inject constructor(
    apiFactory: ApiFactory,
) {

    private val api = apiFactory.create(ProfileApi::class.java)

    suspend fun getProfileData(userId: Long?): ProfileDataResponse {
        return api.getData(userId = userId)
    }

    suspend fun changeAvatar(file: File) {
        return api.changeAvatar(
            photo = MultipartBody.Part.createFormData(
                name = "photo",
                filename = file.name,
                body = file.asRequestBody("image/*".toMediaTypeOrNull())
            ),
        )
    }
}
