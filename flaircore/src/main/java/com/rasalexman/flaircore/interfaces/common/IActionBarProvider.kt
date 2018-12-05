package com.rasalexman.flaircore.interfaces.common

interface IActionBarProvider<out AB, in T> {
    fun getSupportActionBar():AB?
    fun setSupportActionBar(toolbar:T?)
}
