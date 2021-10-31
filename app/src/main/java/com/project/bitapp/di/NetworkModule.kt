package com.project.bitapp.di

import android.content.Context
import androidx.databinding.library.BuildConfig
import com.project.bitapp.BitAppApplication
import com.project.bitapp.data.api.PairListingApi
import com.project.bitapp.data.api.SocketService
import com.project.bitapp.data.repositoryimpl.PairListingRepoImpl
import com.project.bitapp.domain.repository.PairListingRepo
import com.project.bitapp.domain.usecase.GetPairListingUseCase
import com.project.bitapp.utils.BASE_URL
import com.project.bitapp.utils.SOCKET_BASE_ADDRESS
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient
            .Builder()
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providePairListingApi(retrofit: Retrofit): PairListingApi =
        retrofit.create(PairListingApi::class.java)

    @Singleton
    @Provides
    fun providePairListingRepository(
        pairListingApi: PairListingApi
    ): PairListingRepo {
        return PairListingRepoImpl(pairListingApi)
    }
    @Singleton
    @Provides
    fun providePairListingUseCase(getPairListingRepo: PairListingRepo): GetPairListingUseCase {
        return GetPairListingUseCase(getPairListingRepo)
    }

    @Provides
    fun providesApplication(@ApplicationContext context: Context): BitAppApplication {
        return context as BitAppApplication
    }

    @Provides
    fun provideMoshi(adapterFactory: KotlinJsonAdapterFactory): Moshi {
        return Moshi.Builder()
            .add(adapterFactory)
            .build()
    }

    @Provides
    fun provideMoshiAdapterFactory(moshi: Moshi): MoshiMessageAdapter.Factory {
        return MoshiMessageAdapter.Factory(moshi)
    }

    @Provides
    fun provideJsonAdapterFactory(): KotlinJsonAdapterFactory {
        return KotlinJsonAdapterFactory()
    }

    @Provides
    fun providesRxJava2StreamAdapterFactory(): RxJava2StreamAdapterFactory {
        return RxJava2StreamAdapterFactory()
    }

    @Provides
    fun provideScarlet(
        application: BitAppApplication,
        client: OkHttpClient,
        moshiMessageAdapter: MoshiMessageAdapter.Factory,
        streamAdapterFactory: RxJava2StreamAdapterFactory
    ): Scarlet {
        return Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(SOCKET_BASE_ADDRESS))
            .addMessageAdapterFactory(moshiMessageAdapter)
            .addStreamAdapterFactory(streamAdapterFactory)
            .lifecycle(AndroidLifecycle.ofApplicationForeground(application))
            .build()
    }

    @Provides
    fun providesSocketService(scarlet: Scarlet): SocketService {
        return scarlet.create()
    }
}
