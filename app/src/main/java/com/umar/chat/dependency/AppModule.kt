package com.umar.chat.dependency

import com.umar.chat.data.network.WebsocketService
import com.umar.chat.repository.WebsocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWebsocketService(): WebsocketService {
        return WebsocketService()
    }

    @Provides
    @Singleton
    fun provideWebsocketRepository(websocketService: WebsocketService): WebsocketRepository {
        return WebsocketRepository(websocketService)
    }
}