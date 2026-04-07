package com.demo.tictactoe.core 

sealed class Resource<out T> {
    data class Data<T>(val value: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}