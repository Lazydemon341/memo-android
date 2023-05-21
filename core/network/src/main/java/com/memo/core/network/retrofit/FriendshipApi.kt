package com.memo.core.network.retrofit

import com.memo.core.network.NetworkFriendshipDataSource
import com.memo.core.network.model.friendship.AcceptFriendshipRequestBody
import com.memo.core.network.model.friendship.DeclineFriendshipRequestBody
import com.memo.core.network.model.friendship.FriendsResponse
import com.memo.core.network.model.friendship.FriendshipRequestsResponse
import com.memo.core.network.model.friendship.RequestFriendshipBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

interface FriendshipApi {

    @POST("friendship/request")
    suspend fun request(@Body data: RequestFriendshipBody)

    @GET("friendship/incoming_requests")
    suspend fun incomingRequests(): FriendshipRequestsResponse

    @GET("friendship/friends")
    suspend fun friends(): FriendsResponse

    @POST("friendship/decline")
    suspend fun decline(@Body data: DeclineFriendshipRequestBody)

    @POST("friendship/accept")
    suspend fun accept(@Body data: AcceptFriendshipRequestBody)

    @GET("friendship/delete_friend")
    suspend fun deleteFriend(@Query("friend_id") friendId: Long)
}

@Singleton
class RetrofitFriendshipDataSource @Inject constructor(
    apiFactory: ApiFactory,
) : NetworkFriendshipDataSource {

    private val api = apiFactory.create(FriendshipApi::class.java)

    override suspend fun request(data: RequestFriendshipBody) {
        return api.request(data)
    }

    override suspend fun incomingRequests(): FriendshipRequestsResponse {
        return api.incomingRequests()
    }

    override suspend fun friends(): FriendsResponse {
        return api.friends()
    }

    override suspend fun decline(data: DeclineFriendshipRequestBody) {
        return api.decline(data)
    }

    override suspend fun accept(data: AcceptFriendshipRequestBody) {
        return api.accept(data)
    }

    override suspend fun deleteFriend(friendId: Long) {
        return api.deleteFriend(friendId)
    }
}
