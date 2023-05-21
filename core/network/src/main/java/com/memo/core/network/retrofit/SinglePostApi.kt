package com.memo.core.network.retrofit

import com.memo.core.network.NetworkSinglePostDataSource
import com.memo.core.network.model.post.PostDTO
import com.memo.core.network.model.post.PostUploadParams
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface SinglePostApi {

    @GET("post/get")
    suspend fun getPostById(@Query("post_id") postId: Long): PostDTO

    @POST("post/upload")
    suspend fun upload(@Body postUploadParams: PostUploadParams)
}

@Singleton
class RetrofitSinglePostDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkSinglePostDataSource {

    private val api = apiFactory.create(SinglePostApi::class.java)

    override suspend fun getPostById(postId: Long): PostDTO {
        return api.getPostById(postId)
    }

    override suspend fun upload(postUploadParams: PostUploadParams) {
        return api.upload(postUploadParams)
    }
}
