package com.example.newtrackmed.data

import com.example.newtrackmed.data.model.DoseViewData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    data object Loading : Result<Nothing>
}

//fun <T> Flow<T>.asResult(): Flow<Result<T>> {
//    return this
//        .map<T, Result<T>> {
//            Result.Success(it)
//        }
//        .onStart { emit(Result.Loading) }
//        .catch { emit(Result.Error(it)) }
//}
//
//fun <T> Result<T>.successOr(fallback: T): T {
//    return (this as? Result.Success<T>)?.data ?: fallback
//}



private const val RETRY_TIME_IN_MILLIS = 15_000L


//
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return this
        .map<T, Result<T>> {
            Result.Success(it)
        }
        .onStart { emit(Result.Loading) }
        .retryWhen { cause, _ ->
            if (cause is IOException) {
                emit(Result.Error(cause))

                delay(RETRY_TIME_IN_MILLIS)
                true
            } else {
                false
            }
        }
        .catch { emit(Result.Error(it)) }
}