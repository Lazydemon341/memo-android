package com.memo.core.data.repository

import com.memo.core.model.user.AuthParams
import com.memo.core.model.user.SignUpParams

interface UserRepository {

    suspend fun register(signUpParams: SignUpParams): Result<Unit>

    suspend fun confirmRegistration(confirmationCode: Int): Result<Unit>

    suspend fun auth(params: AuthParams): Result<Unit>

    suspend fun logout(): Result<Unit>
}
