package com.rasalexman.flaircore.ext

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.rasalexman.flaircore.interfaces.IMediator


////------ SOME USEFUL EXTENSION FUNCTIONS

/**
 * Get app context resources
 */
val IMediator.resources: Resources
    get() = appContext.resources

/**
 * Drawable from resource id
 *
 * @param resourceId - drawable resource Ids
 * @param init - init drawable function
 */
fun IMediator.drawable(@DrawableRes resourceId: Int, init: ((Drawable?) -> Drawable?)? = null): Drawable? = init?.let {
    it(ContextCompat.getDrawable(appContext, resourceId))
} ?: ContextCompat.getDrawable(appContext, resourceId)

/***
 * Custom View For somethings like rounded drawable
 *
 * @param colorRes - color like 0x00ff00
 * @param corners - drawable corner round
 * @param withStroke - need stroke
 * @param strokeColor - stroke color
 * @param strokeWeight - stroke width
 * */
fun IMediator.roundedBg(colorRes: Int, corners: Float = 100f, withStroke: Boolean = false, strokeColor: Int = Color.LTGRAY, strokeWeight: Int = 2) = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = corners
    setColor(colorRes)
    if (withStroke) setStroke(strokeWeight, strokeColor)
}

/**
 * Find the view in `viewComponent` by given resource Id
 *
 * @param resourceId
 * Resource view identificator like `R.id.my_button_id`
 */
inline infix fun <reified T : View> IMediator.view(resourceId: Int): Lazy<T?> = lazy {
    viewComponent?.findViewById(resourceId) as? T
}


/**
 * Color from resources id
 */
fun IMediator.color(@ColorRes resourceId: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) appContext.getColor(resourceId) else this.resources.getColor(resourceId)

/**
 * For adaptive procent width
 */
fun IMediator.wdthProc(proc: Float): Int {
    return (resources.displayMetrics.widthPixels * proc).toInt()
}

/**
 * For adaptive procent height
 */
fun IMediator.hdthProc(proc: Float): Int {
    return (resources.displayMetrics.heightPixels * proc).toInt()
}

/**
 * Show toast with resource id
 */
fun IMediator.toast(@StringRes resId: Int, time: Int = Toast.LENGTH_LONG){
    Toast.makeText(activity, resId, time).show()
}

/**
 * Show toast with message string
 */
fun IMediator.toast(message: String, time: Int = Toast.LENGTH_LONG){
    Toast.makeText(activity, message, time).show()
}

/**
 * Get a string resources from app context by it id
 */
fun IMediator.string(resId: Int): String = appContext.getString(resId)