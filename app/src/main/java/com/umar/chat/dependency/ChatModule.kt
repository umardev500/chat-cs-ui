package com.umar.chat.dependency

import com.umar.chat.data.network.ChatApiService
import com.umar.chat.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {
    @Provides
    @Singleton
    fun provideChatApiService(): ChatApiService {
        return ChatApiService()
    }

    @Provides
    @Singleton
    fun provideChatRepository(chatApiService: ChatApiService): ChatRepository {
        return ChatRepository(chatApiService)
    }
}