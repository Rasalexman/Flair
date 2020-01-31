package com.mincor.flair.views

import android.content.Context
import com.google.android.material.tabs.TabLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.mincor.flair.R
import com.mincor.flair.views.pager.PageOneMediator
import com.mincor.flair.views.pager.PageThreeMediator
import com.mincor.flair.views.pager.PageTwoMediator
import com.rasalexman.flaircore.common.adapters.FlairPagerAdapter
import com.rasalexman.flaircore.ext.log
import com.rasalexman.flaircore.interfaces.removeMediator
import com.rasalexman.flairreflect.mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class ViewPagerMediator : ToolbarMediator() {

    override var hashBackButton: Boolean = true

    override fun createLayout(context: Context): View = ViewPagerUI().createView(AnkoContext.create(context, this))

    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null

    override fun onCreatedView(view: View) {
        super.onCreatedView(view)
        log { "HELLO FROM PAGER" }

        viewPager?.adapter = FlairPagerAdapter(
                listOf(mediator<PageOneMediator>(), mediator<PageTwoMediator>(), mediator<PageThreeMediator>()),
                listOf("TabOne", "TabTwo", "TabThree"))

        tabLayout?.setupWithViewPager(viewPager)
    }

    override fun onDestroyView() {
        removeMediator<PageOneMediator>()
        removeMediator<PageTwoMediator>()
        removeMediator<PageThreeMediator>()
        viewPager?.adapter = null
        tabLayout?.setupWithViewPager(null)
        viewPager = null
        tabLayout = null
        super.onDestroyView()
    }

    class ViewPagerUI : AnkoComponent<ViewPagerMediator> {
        override fun createView(ui: AnkoContext<ViewPagerMediator>): View = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)
                ui.owner.toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "ViewPagerMediator"
                    backgroundResource = R.color.colorPrimary
                }

                ui.owner.tabLayout = tabLayout {
                    tabMode = TabLayout.MODE_FIXED
                }.lparams(matchParent)

                ui.owner.viewPager = viewPager {

                }.lparams(matchParent, matchParent)
            }
        }
    }
}