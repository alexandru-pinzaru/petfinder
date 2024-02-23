package com.example.petfinder.nearby.presentation.events

class ErrorEvent<out T>(private val content: T) {

    private var isHandled = false

    fun getIfNotHandled(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            content
        }
    }
}