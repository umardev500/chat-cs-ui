package com.umar.chat.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class CommonModel(
    val success: Boolean = false,
    val message: String,
    val data: JsonElement? = null
)