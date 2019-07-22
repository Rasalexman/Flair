package com.rasalexman.flaircore.interfaces.common

/**
 * Provider interface adapter for action bar
 */
interface IActionBarProvider<out AB, in T> {
    /**
     * Get Support Action bar
     */
    fun getSupportActionBar():AB?

    /**
     * Set current action bar
     */
    fun setSupportActionBar(toolbar:T?)
}
