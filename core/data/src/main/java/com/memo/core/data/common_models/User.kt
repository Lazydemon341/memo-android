package com.memo.core.data.common_models

import com.memo.core.model.user.AuthParams
import com.memo.core.model.user.SignUpParams
import com.memo.core.network.model.user.AuthBody
import com.memo.core.network.model.user.RegisterUserBody

fun SignUpParams.asNetworkModel() = RegisterUserBody(
    name = name,
    surname = surname,
    email = email,
    password = password,
)

fun AuthParams.asNetworkModel() = AuthBody(
    email = email,
    password = password,
)
