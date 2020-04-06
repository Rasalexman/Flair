package com.rasalexman.flaircore.common.bundle

import android.os.Bundle
import android.os.Parcelable
import com.rasalexman.flaircore.interfaces.IMediator
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Save state provider for delegation properties
 *
 * @param stateBundleProvider - bundle for save value in
 */
abstract class InstanceStateProvider<T>(
        private val stateBundleProvider: () -> Bundle
) : ReadWriteProperty<Any?, T> {

    /**
     * Bundle factory method
     */
    protected val stateBundle by lazy {
        stateBundleProvider()
    }

    /**
     * Set value to bundle or throw IllegalArgumentException
     */
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (value) {
            null -> stateBundle.remove(property.name)
            is Int -> stateBundle.putInt(property.name, value)
            is Long -> stateBundle.putLong(property.name, value)
            is Float -> stateBundle.putFloat(property.name, value)
            is Double -> stateBundle.putDouble(property.name, value)
            is String -> stateBundle.putString(property.name, value)
            is Boolean -> stateBundle.putBoolean(property.name, value)
            is Bundle -> stateBundle.putBundle(property.name, value)
            is Parcelable -> stateBundle.putParcelable(property.name, value)
            is Serializable -> stateBundle.putSerializable(property.name, value)
            // Add more if you need
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}

/**
 * Nullable delegation with bundle save state value
 *
 * @param defaultValue - default value
 * @param stateBundleProvider - factory for provider bundle
 */
class NullableStateProvider<T : Any?>(
        private val defaultValue: T,
        stateBundleProvider: () -> Bundle
) : InstanceStateProvider<T>(stateBundleProvider) {
    /**
     * Get provider value
     */
    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
            stateBundle.get(property.name) as T ?: defaultValue
}

/*
class NotNullStateProvider<T : Any>(
        private val defaultValue: T,
        stateBundleProvider: () -> Bundle
) : InstanceStateProvider<T>(stateBundleProvider) {
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T =
            stateBundle.get(property.name) as T? ?: defaultValue
}
*/

/**
 * Put value into bundle for save state
 * @param defaultValue
 */
inline fun <reified T : Any?> IMediator.bundleValue(defaultValue: T) = NullableStateProvider<T>(defaultValue) {
    this.arguments
}