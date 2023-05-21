package com.memo.core.datastore.user_tokens

import androidx.datastore.core.DataStore
import java.io.IOException
import javax.inject.Inject

class UserTokensDataSource @Inject constructor(
    private val dataStore: DataStore<UserTokens>,
) {

    val tokensFlow = dataStore.data

    @Throws(IOException::class)
    suspend fun setTokens(tokens: UserTokens?) {
        dataStore.updateData { tokens ?: UserTokens() }
    }
}
