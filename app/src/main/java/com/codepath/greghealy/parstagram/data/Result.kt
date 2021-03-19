package com.codepath.greghealy.parstagram.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result {

    data class Success(val data: String) : Result()
    data class Error(val exception: Exception) : Result()

    override fun toString(): String {
        return when (this) {
            is Success -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}