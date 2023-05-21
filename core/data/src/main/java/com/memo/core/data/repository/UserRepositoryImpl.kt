package com.memo.core.data.repository

import com.memo.core.data.common_models.asNetworkModel
import com.memo.core.datastore.user_tokens.UserTokensDataSource
import com.memo.core.model.user.AuthParams
import com.memo.core.model.user.SignUpParams
import com.memo.core.network.NetworkUserDataSource
import com.memo.core.network.model.user.ConfirmUserBody
import com.memo.core.network.model.user.toLocalModel
import com.memo.core.utils.network.retryOnNetworkOrServerErrors
import com.memo.core.utils.suspendRunCatching
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val network: NetworkUserDataSource,
    private val tokensDataSource: UserTokensDataSource,
) : UserRepository {

    override suspend fun register(signUpParams: SignUpParams): Result<Unit> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                network.register(signUpParams.asNetworkModel())
            }
            if (!response.registered) {
                throw IllegalStateException("Registration failed")
            }
            tokensDataSource.setTokens(response.tokens.toLocalModel())
        }
    }

    override suspend fun confirmRegistration(confirmationCode: Int): Result<Unit> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                network.confirm(ConfirmUserBody(confirmationCode))
            }
            if (!response.isConfirmed) {
                throw IllegalStateException("Confirmation failed")
            }
        }
    }

    override suspend fun auth(params: AuthParams): Result<Unit> {
        return suspendRunCatching {
            val response = retryOnNetworkOrServerErrors {
                network.auth(params.asNetworkModel())
            }
            tokensDataSource.setTokens(response.toLocalModel())
        }
    }
}
