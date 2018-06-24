package com.mincor.flairframework.interfaces.common

interface IActionBarProvider<out AB, in T> {
    fun getSupportActionBar():AB?
    fun setSupportActionBar(toolbar:T?)
}
