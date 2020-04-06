package com.rasalexman.flaircore.interfaces

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * Created by a.minkin on 21.11.2017.
 */
interface IMediator : INotifier {
    /**
     * Current view for IMediator instance
     */
    var viewComponent: View?
    /**
     * Does options menu activated
     */
    var hasOptionalMenu: Boolean
    /**
     * If we need to hide options menu
     */
    var hideOptionalMenu: Boolean
    /**
     * Does IMedaitor `viewComponent` added to root container
     */
    var isAdded: Boolean
    /**
     * Does IMediator `viewComponent` was destroyed
     */
    var isDestroyed: Boolean

    /**
     * Does this mediator need to be added in backstack.
     * It's true by default
     * so framework is automatically navigate you by his backstack store
     */
    var isAddToBackStack:Boolean

    /**
     * Current IMediator name for retrieving
     */
    var mediatorName: String?

    /**
     * Bundle arguments
     */
    val arguments: Bundle
    /**
     * List of notification names that this IMediator is listening for
     */
    val listNotificationInterests: MutableList<String>

    /**
     * When current View has attached your menu
     */
    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)

    /**
     * Fragment legacy menu creation
     */
    fun onPrepareOptionsMenu(menu: Menu)

    /**
     * Menu Item Selection section
     */
    fun onOptionsItemSelected(item: MenuItem): Boolean

    /**
     * Respond function from current activity
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    /**
     * WHEN REQUESTED PERMISSIONS IS GRANTED
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)

    /**
     * Main initialize ViewComponent Function
     *
     * @param context - context for create layout with
     */
    fun createLayout(context: Context): View

    /**
     * It's called before view created
     * If we need to prepare some things before view is created ex get saved params from bundle
     */
    fun onPrepareCreateView()

    /**
     * Called by the create mediator view once
     *
     * @param view - created view
     */
    fun onCreatedView(view: View)

    /**
     * Called when mediator added to view container
     *
     * @param view - the view instance that added to container
     */
    fun onAddedView(view: View)

    /**
     * Called when mediator view removed from parent
     *
     * @param view - removed view instance
     */
    fun onRemovedView(view: View)

    /**
     * When view is destroyed or equal to null
     */
    fun onDestroyView()


    /**
     * Called by the View when the Mediator is registered.
     */
    fun onRegister()

    /**
     * Called by the View when the Mediator is removed.
     */
    fun onRemove()

    /**
     * Handle the hardware back button
     *
     * @param animation
     * Current animation changer
     */
    fun handleBackButton(animation: IAnimator? = null): Boolean

    /**
     * Called when animation is starting
     *
     * @param isShow
     * Flag that indicates is this starting a showing animation for current mediator
     */
    fun onAnimationStart(isShow: Boolean)

    /**
     * Called when animation is finished
     *
     * @param isShow
     * Flag that indicates is this starting a showing animation for current mediator
     */
    fun onAnimationFinish(isShow: Boolean)
}

/**
 * Inflate current view component
 */
fun IMediator.inflateView(layoutId: Int): View = android.view.LayoutInflater.from(activity).inflate(layoutId, null)

/**
 * Start Activity with given intant and request code
 */
fun IMediator.startActivityForResult(intent: Intent, requestCode: Int, options: Bundle? = null) {
    facade.view.startActivityForResult(intent, requestCode, options)
}

/**
 * Start another activity with bundle options or not
 */
fun IMediator.startActivity(intent: Intent, bundle: Bundle? = null) {
    bundle?.let {
        activity.startActivity(intent, it)
    } ?: activity.startActivity(intent)
}

/**
 * Request Permissions from user
 */
fun IMediator.requestPermissions(permissions: Array<String>, requestCode: Int) {
    facade.view.requestPermissions(permissions, requestCode)
}

/**
 * Check the given permission to be approved by user or system
 */
fun IMediator.checkSelfPermission(permissionToCheck: String): Int = facade.view.checkSelfPermission(permissionToCheck)

/**
 * Should we show permission description dialog for user
 */
fun IMediator.shouldShowRequestPermissionRationale(permission: String): Boolean = facade.view.shouldShowRequestPermissionRationale(permission)

/**
 * Register a list of `INotification` interests.
 *
 * @param listNotification
 * array of string notification names
 *
 * @param notificator
 * the callback function
 *
 */
fun IMediator.registerListObservers(listNotification: List<String>, notificator: INotificator): IMediator {
    listNotification.forEach {
        registerObserver(it, notificator)
    }
    return this
}

/**
 * Register observer function with notifName
 *
 * @param notifName
 * Name of notification that recieve notificator
 *
 * @param notificator
 * This is simple closure function callback
 *
 */
fun IMediator.registerObserver(notifName: String, notificator: INotificator): IMediator {
    listNotificationInterests.add(notifName)
    facade.registerObserver(notifName, notificator)
    return this
}

/**
 * Remove observer by given notification name
 *
 * @param notifName
 * Name of notification that recieve notificator
 */
fun IMediator.removeObserver(notifName: String) {
    listNotificationInterests.remove(notifName)
    facade.removeObserver(notifName, this.multitonKey)
}

/**
 * Remove all notifications from current IMediator implementation instance
 */
fun IMediator.removeAllObservers() {
    listNotificationInterests.forEach {
        facade.removeObserver(it, this.multitonKey)
    }
    listNotificationInterests.clear()
}

/**
 * Add current mediator to view stage
 *
 * @param popLast
 * Flag that indicates to need remove last showing mediator from backstack
 */
fun IMediator.show(animation: IAnimator? = null, popLast: Boolean = false) {
    facade.showMediator<IMediator>(this.mediatorName ?: this.toString(), popLast, animation)
}

/**
 * Remove current mediator from view stage
 *
 * @param animation
 * Current hide animation
 *
 * @param popIt
 * Flag that indicates to need remove current mediator from backstack
 */
fun IMediator.hide(animation: IAnimator? = null, popIt: Boolean = false) {
    facade.hideMediator(this.mediatorName ?: this.toString(), popIt, animation)
}


/**
 * Hide current mediator and remove it from backstack
 * Then show last added mediator from backstack
 *
 * @param animation
 * Current hiding animation for pop
 */
fun IMediator.popToBack(animation: IAnimator? = null) {
    facade.popMediator(this.mediatorName ?: this.toString(), animation)
}

/**
 * Retrieve lazy mediator core by given generic class
 *
 * @param mediatorName
 * Mediator name to be retrieved by lazy function
 */
inline fun <reified T : IMediator> IMediator.mediatorLazy(mediatorName: String? = null): Lazy<T> = lazy {
    mediator<T>(mediatorName)
}

/**
 * Retrieve lazy mediator core by given generic class
 *
 * @param mediatorName
 * Mediator name to be retrieved
 */
inline fun <reified T : IMediator> IMediator.mediator(mediatorName: String? = null): T = facade.retrieveMediator(mediatorName)

/**
 * Remove an `IMediator` from the `View` core.
 */
inline fun <reified T : IMediator> IMediator.removeMediator(mediatorName: String? = null) {
    facade.removeMediator<T>(mediatorName)
}


/**
 * GO Back to given mediatorName(high priority) or Generic class name.
 *
 * @param mediatorName
 * Mediator to which you go back, so if there is no registered mediatorLazy by this name new instance is create as back stacked
 *
 * @param animation
 * Current hiding animation for pop
 */
inline fun <reified T : IMediator> IMediator.popTo(mediatorName: String? = null, animation: IAnimator? = null) {
    facade.retrieveMediator<T>(mediatorName).show(animation, true)
}

/**
 * Show the given generic mediatorLazy or by name
 *
 * @param mediatorName
 * Mediator name to be showed
 *
 * @param builder
 * Mediator builder lambda use to instantiate
 */
inline fun <reified T : IMediator> IMediator.showMediator(animation: IAnimator? = null, mediatorName: String? = null, noinline builder:(()->T)? = null) {
    return when {
        facade.hasMediator<T>(mediatorName) -> facade.retrieveMediator<T>(mediatorName).show(animation)
        builder != null -> facade.registerMediator(mediatorName, builder).show(animation)
        else -> throw RuntimeException("You need to register your mediator first with `builder` function or use reflection package instead")
    }
}


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
 * Get gradient for Background
 *
 * @param colors - color array for start and end
 * @param orient - orientation
 * @param corners - corners radius
 * @param withStroke - stroke if needed
 * @param strokeColor - stroke color
 * @param strokeWeight - weight of strokes
 */
fun gradientBg(
        colors: Array<Int>,
        orient: GradientDrawable.Orientation = GradientDrawable.Orientation.BOTTOM_TOP,
        corners: Float = 0f,
        withStroke: Boolean = false,
        strokeColor: Int = Color.LTGRAY,
        strokeWeight: Int = 2
): GradientDrawable = GradientDrawable(orient, colors.toIntArray()).apply {
    shape = GradientDrawable.RECTANGLE
    cornerRadius = corners
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
fun IMediator.color(@ColorRes resourceId: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) appContext.getColor(resourceId) else appContext.resources.getColor(resourceId)

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
 * Get a string resourses from app context by it id
 */
fun IMediator.string(resId: Int): String = appContext.getString(resId)