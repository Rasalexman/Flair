package com.rasalexman.flaircoroutines.base

import com.rasalexman.flaircore.patterns.command.SimpleCommand
import com.rasalexman.flaircoroutines.managers.CoroutinesManager
import com.rasalexman.flaircoroutines.managers.ICoroutinesManager

/**
 * Base Command Async class
 * Used for async{} block calls
 *
 * @param coroutinesManager
 * Any CoroutinesManager instance. You can use di for this
 */
abstract class CoroutineCommand(private val coroutinesManager: CoroutinesManager = CoroutinesManager()) : SimpleCommand(), ICoroutinesManager by coroutinesManager