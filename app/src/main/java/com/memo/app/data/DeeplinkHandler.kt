package com.memo.app.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.memo.core.network.NetworkFriendshipDataSource
import com.memo.core.network.model.friendship.RequestFriendshipBody
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.trySuspend
import timber.log.Timber
import javax.inject.Inject

class DeeplinkHandler @Inject constructor(
    private val networkFriendshipDataSource: NetworkFriendshipDataSource,
) {

    suspend fun handle(uri: Uri, context: Context) {
        if (uri.host == "friendship" && uri.path == "/add") {
            handleAddFriendDeeplink(uri, context)
        }
    }

    private suspend fun handleAddFriendDeeplink(uri: Uri, context: Context) {
        trySuspend(
            run = {
                val userId = uri.getQueryParameter("id")?.toLong()
                    ?: throw IllegalStateException("no id provided in add friend deeplink")
                retryOnNetworkOrServerErrors {
                    networkFriendshipDataSource.request(RequestFriendshipBody(userId = userId))
                }
                Toast.makeText(context, "Friendship request sent", Toast.LENGTH_LONG).show()
            },
            catch = {
                Timber.e(it, "Failed to handle add friend deeplink")
            }
        )
    }
}
