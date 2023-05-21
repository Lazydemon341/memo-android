package com.memo.friendship.internal.domain

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat.QR_CODE
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.memo.core.datastore.user_tokens.UserTokensDataSource
import com.memo.core.utils.suspendRunCatching
import com.memo.friendship.internal.utils.FriendshipDeeplinks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetRequestQRUseCase @Inject constructor(
    private val userTokensDataSource: UserTokensDataSource,
) {

    private val barcodeEncoder = BarcodeEncoder()

    suspend operator fun invoke(): Result<Bitmap> = withContext(Dispatchers.Default) {
        suspendRunCatching {
            val userId = userTokensDataSource.tokensFlow.first().userId
            val deeplink = FriendshipDeeplinks.getAddFriendDeeplink(userId = userId)
            barcodeEncoder.encodeBitmap(deeplink, QR_CODE, 512, 512)
        }
    }
}
