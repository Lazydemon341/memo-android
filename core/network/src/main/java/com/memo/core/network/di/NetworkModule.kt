package com.memo.core.network.di

import com.memo.core.network.BuildConfig
import com.memo.core.network.NetworkFeedDataSource
import com.memo.core.network.NetworkFriendshipDataSource
import com.memo.core.network.NetworkMapDataSource
import com.memo.core.network.NetworkMemoryDataSource
import com.memo.core.network.NetworkMessagingDataSource
import com.memo.core.network.NetworkPhotoDataSource
import com.memo.core.network.NetworkSinglePostDataSource
import com.memo.core.network.NetworkUserDataSource
import com.memo.core.network.NetworkWidgetDataSource
import com.memo.core.network.interceptor.AuthInterceptor
import com.memo.core.network.interceptor.ErrorsInterceptor
import com.memo.core.network.retrofit.RetrofitFeedDataSource
import com.memo.core.network.retrofit.RetrofitFriendshipDataSource
import com.memo.core.network.retrofit.RetrofitMapDataSource
import com.memo.core.network.retrofit.RetrofitMemoryDataSource
import com.memo.core.network.retrofit.RetrofitMessagingDataSource
import com.memo.core.network.retrofit.RetrofitPhotoDataSource
import com.memo.core.network.retrofit.RetrofitSinglePostDataSource
import com.memo.core.network.retrofit.RetrofitUserDataSource
import com.memo.core.network.retrofit.RetrofitWidgetDataSource
import com.memo.core.network.utils.monitor.ConnectivityManagerNetworkMonitor
import com.memo.core.network.utils.monitor.NetworkMonitor
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        authInterceptor: AuthInterceptor,
        errorsInterceptor: ErrorsInterceptor,
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(errorsInterceptor)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor(Timber::d)
            loggingInterceptor.setLevel(BODY)
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsNetworkModule {

        @Binds
        fun bindNetworkMonitor(impl: ConnectivityManagerNetworkMonitor): NetworkMonitor

        @Binds
        fun bindUserDataSource(impl: RetrofitUserDataSource): NetworkUserDataSource

        @Binds
        fun bindWidgetDataSource(impl: RetrofitWidgetDataSource): NetworkWidgetDataSource

        @Binds
        fun bindPhotoDataSource(impl: RetrofitPhotoDataSource): NetworkPhotoDataSource

        @Binds
        fun bindFriendshipDataSource(impl: RetrofitFriendshipDataSource): NetworkFriendshipDataSource

        @Binds
        fun bindFeedDataSource(impl: RetrofitFeedDataSource): NetworkFeedDataSource

        @Binds
        fun bindMessagingDataSource(impl: RetrofitMessagingDataSource): NetworkMessagingDataSource

        @Binds
        fun bindNetworkMapDataSource(impl: RetrofitMapDataSource): NetworkMapDataSource

        @Binds
        fun bindSinglePostDataSource(impl: RetrofitSinglePostDataSource): NetworkSinglePostDataSource

        @Binds
        fun bindMemoryDataSource(impl: RetrofitMemoryDataSource): NetworkMemoryDataSource
    }
}
