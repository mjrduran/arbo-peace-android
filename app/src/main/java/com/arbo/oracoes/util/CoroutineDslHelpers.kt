package com.arbo.oracoes.util

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun startJob(
    parentScope: CoroutineScope,
    coroutineContext: CoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    parentScope.launch(coroutineContext) {
        supervisorScope {
            block()
        }
    }
}

suspend fun <T> startTask(
    coroutineContext: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    return withContext(coroutineContext) {
        return@withContext block()
    }
}

fun <T> startTaskAsync(
    parentScope: CoroutineScope,
    coroutineContext: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): Deferred<T> {
    return parentScope.async(coroutineContext) {
        return@async supervisorScope {
            return@supervisorScope block()
        }
    }
}




