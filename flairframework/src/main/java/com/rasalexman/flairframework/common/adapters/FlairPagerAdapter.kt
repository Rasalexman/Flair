package com.rasalexman.flairframework.common.adapters

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.rasalexman.flairframework.ext.clear
import com.rasalexman.flairframework.interfaces.IMediator

/**
 * Custom flair pager adapter
 * @param mediators
 * the list of registered mediators ex listOf(mediator<T1>(),mediator<T2>(),mediator<T3>())
 */
open class FlairPagerAdapter(private val mediators:List<IMediator>, private val tabNames:List<String>) : PagerAdapter() {

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == (p1 as? View)
    }

    /**
     * Initialize pager item
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val currentMediator = mediators[position]
        currentMediator.viewComponent = currentMediator.viewComponent ?: let {
            val view = currentMediator.createLayout(container.context)
            currentMediator.onCreatedView(view)
            view
        }
        val viewLayout = currentMediator.viewComponent!!
        container.addView(viewLayout)
        currentMediator.onAddedView(viewLayout)
        return viewLayout
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        (view as? View)?.let {
            val currentMediator = mediators[position]
            container.removeView(it)
            currentMediator.onRemovedView(it)
            (it as? ViewGroup)?.clear()
            currentMediator.viewComponent = null
            currentMediator.onDestroyView()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(tabNames.size > position) tabNames[position] else ""
    }

    override fun getCount(): Int {
        return mediators.size
    }
}