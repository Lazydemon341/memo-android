package com.memo.photo.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.AppWidgetTarget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class PhotoWidget : AppWidgetProvider() {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Inject
    lateinit var widgetRepository: PhotoWidgetRepository

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onDisabled(context: Context) {
        coroutineScope.cancel()
    }

    private fun getImageSize(appWidgetProviderInfo: AppWidgetProviderInfo): Int {
        val width = appWidgetProviderInfo.minWidth * 4
        val height = appWidgetProviderInfo.minHeight * 4
        return minOf(width, height)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        coroutineScope.launch {
            // TODO pending intent on click
            val remoteViews = withContext(Dispatchers.Main) {
                RemoteViews(context.packageName, R.layout.photo_widget)
            }

            val lastPhotoInfoResult = widgetRepository.getLastPhoto()
            if (lastPhotoInfoResult.isFailure) {
                return@launch
            }
            val lastPhotoInfo = lastPhotoInfoResult.getOrThrow()

            val mainPhotoRequestOptions =
                RequestOptions()
                    .override(getImageSize(appWidgetManager.getAppWidgetInfo(appWidgetId)))
                    .transform(RoundedCorners(context.resources.getDimensionPixelSize(R.dimen.widget_corners_radius)))
            val mainPhotoTarget = AppWidgetTarget(context, R.id.photo_widget_photo, remoteViews, appWidgetId)
            Glide.with(context)
                .asBitmap()
                .load(lastPhotoInfo.photoUrl)
                .placeholder(R.drawable.widget_profile_icon_preview)
                .error(R.drawable.widget_profile_icon_preview)
                .apply(mainPhotoRequestOptions)
                .into(mainPhotoTarget)

            val profilePhotoRequestOptions =
                RequestOptions()
                    .override(200)
                    .transform(CircleCrop())
            val profilePhotoTarget = AppWidgetTarget(context, R.id.photo_widget_profile_icon, remoteViews, appWidgetId)
            Glide.with(context)
                .asBitmap()
                .load(lastPhotoInfo.publisherProfileImageUrl)
                .placeholder(R.drawable.widget_preview_background)
                .error(R.drawable.widget_preview_background)
                .apply(profilePhotoRequestOptions)
                .into(profilePhotoTarget)

            withContext(Dispatchers.Main) {
                if (lastPhotoInfo.caption.isNullOrEmpty()) {
                    remoteViews.setViewVisibility(R.id.photo_widget_caption_text, View.GONE)
                    remoteViews.setTextViewTextSize(R.id.photo_widget_location_text, COMPLEX_UNIT_SP, 12f)
                } else {
                    remoteViews.setViewVisibility(R.id.photo_widget_caption_text, View.VISIBLE)
                    remoteViews.setTextViewText(R.id.photo_widget_caption_text, "✍️ ${lastPhotoInfo.caption}")
                    remoteViews.setTextViewTextSize(R.id.photo_widget_location_text, COMPLEX_UNIT_SP, 8f)
                }
                if (lastPhotoInfo.location.isNullOrEmpty()) {
                    remoteViews.setViewVisibility(R.id.photo_widget_location_text, View.GONE)
                } else {
                    remoteViews.setViewVisibility(R.id.photo_widget_location_text, View.VISIBLE)
                    remoteViews.setTextViewText(R.id.photo_widget_location_text, "\uD83D\uDCCD️ ${lastPhotoInfo.location}")
                }
                appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
            }
        }
    }
}
