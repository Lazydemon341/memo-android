package com.memo.core.network

import com.memo.core.network.model.user.AuthBody
import com.memo.core.network.model.user.ConfirmUserBody
import com.memo.core.network.model.user.ConfirmUserResponse
import com.memo.core.network.model.user.RefreshBody
import com.memo.core.network.model.user.RegisterUserBody
import com.memo.core.network.model.user.RegisterUserResponse
import com.memo.core.network.model.user.UserTokensResponse

interface NetworkUserDataSource {

    suspend fun register(data: RegisterUserBody): RegisterUserResponse

    suspend fun confirm(data: ConfirmUserBody): ConfirmUserResponse

    suspend fun auth(data: AuthBody): UserTokensResponse

    suspend fun refresh(data: RefreshBody): UserTokensResponse
}
