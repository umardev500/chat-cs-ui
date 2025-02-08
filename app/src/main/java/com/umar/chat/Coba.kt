package com.umar.chat

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun simpleFlow(): Flow<Int> = flow {
    for (i in 1..5) {
        emit(i) // Emitting values 1 to 5
        delay(1000)
    }
}

fun main() = runBlocking {
    simpleFlow().collect { value ->
        println("Received: $value") // Collecting emitted values
    }
    println("ended")
}
