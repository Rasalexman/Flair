package com.rasalexman.flairframework.ext

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.rasalexman.flairframework.BuildConfig
import com.rasalexman.flairframework.interfaces.FacadeInitializer
import com.rasalexman.flairframework.interfaces.IFacade
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Create instance of a given KClass with parameters
 * It used for create an instance of Proxy/Commands/Mediators classes
 *
 * @param values
 * constructor parameters as vararg
 */
fun <T : Any> KClass<T>.createInstance(values: List<Any>? = null): T {
    val cons = this.primaryConstructor!!
    val valmap = mutableMapOf<KParameter, Any>()
    if (values?.isNotEmpty() == true) {
        cons.parameters.forEachIndexed { index, kParameter ->
            valmap[kParameter] = values[index]
        }
    }
    return cons.callBy(valmap)
}

/**
 * Inject given params to constructor instance members
 */
fun Any.injectInConstructor(consParams: List<Any>? = null): Any {
    consParams?.let { params ->
        val members = this.javaClass.kotlin.memberProperties as? List<KProperty1<Any, Any>>
        members?.let {
            val ms = it.size
            params.forEachIndexed { index, param ->
                if (ms > index) {
                    val kProperty1 = it[index]
                    val paramName = param.className()
                    val propName = kProperty1.returnType.toString().replace("?", "")
                    if (paramName == propName) {
                        (kProperty1 as? KMutableProperty1<Any, Any>)?.set(this, param)
                    }
                }
            }
        }
    }
    return this
}

/**
 * Remove child from parent if it has
 */
fun View.removeFromParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

/**
 * Ext function
 */
fun Any.className(): String {
    val clazz = this as? KClass<*> ?: this.javaClass.kotlin
    return clazz.qualifiedName ?: clazz.java.name
}

/**
 * System function
 */
fun ImageView.clear() {
    this.setImageResource(0)
    this.setImageBitmap(null)
    this.setImageDrawable(null)
    this.background = null
}

/**
 * Clear button
 */
fun Button.clear() {
    this.background = null
    this.setOnClickListener(null)
    this.text = null
}

/**
 * Clear TextView
 */
fun TextView.clear() {
    this.text = null
    this.setOnClickListener(null)
    this.setCompoundDrawables(null, null, null, null)
    this.background = null
}

/**
 * System clear view function
 */
fun ViewGroup.clear() {
    var childView: View
    repeat(this.childCount) {
        childView = this.getChildAt(it)
        when (childView) {
            is ViewGroup -> (childView as ViewGroup).clear()
            is ImageView -> (childView as ImageView).clear()
            is Button -> (childView as Button).clear()
            is TextView -> (childView as TextView).clear()
            is CompoundButton -> {
                (childView as CompoundButton).text = null
                (childView as CompoundButton).setOnCheckedChangeListener(null)
            }
        }
    }
}

/**
 * Hide or show view by boolean flag
 */
var android.view.View.visible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

/**
 * Hide view with optional param
 * @param gone
 * Does the view removed from parent (optional true)
 */
fun android.view.View.hide(gone: Boolean = true) {
    visibility = if (gone) View.GONE else View.INVISIBLE
}

/**
 * Show the view
 */
fun android.view.View.show() {
    visibility = View.VISIBLE
}

/**
 * Instantiate new Facade for given core name
 *
 * @param key
 * Core Name, used to get IFacade instance
 *
 * @param block
 * initialization function. This is a starting point to register Proxy/Mediators/Commands
 */
fun Context.flair(key: String = IFacade.DEFAULT_KEY, block: FacadeInitializer? = null): IFacade = IFacade.core(key, this, block)

/**
 * Log any messages with given lambda func
 */
inline fun log(lambda: () -> String) {
    if (BuildConfig.DEBUG) {
        Log.d("FLAIR_LOG ------>", lambda())
    }
}