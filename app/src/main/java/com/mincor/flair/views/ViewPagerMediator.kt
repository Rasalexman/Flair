package com.mincor.flair.views

import android.content.Context
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.View
import com.mincor.flair.R
import com.mincor.flair.views.pager.PageOneMediator
import com.mincor.flair.views.pager.PageThreeMediator
import com.mincor.flair.views.pager.PageTwoMediator
import com.mincor.flairframework.common.adapters.FlairPagerAdapter
import com.mincor.flairframework.interfaces.mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.viewPager

class ViewPagerMediator : ToolbarMediator() {

    override fun createLayout(context: Context): View = ViewPagerUI().createView(AnkoContext.Companion.create(context, this))

    private var viewPager:ViewPager? = null
    private var tabLayout:TabLayout? = null

    override fun onCreatedView(view: View) {
        super.onCreatedView(view)
        setHomeButtonEnable()
        com.mincor.flairframework.ext.log { "HELLO FROM PAGER" }

        viewPager?.adapter = FlairPagerAdapter(
                listOf(mediator<PageOneMediator>(), mediator<PageTwoMediator>(), mediator<PageThreeMediator>()),
                listOf("TabOne", "TabTwo", "TabThree"))

        tabLayout?.setupWithViewPager(viewPager)
    }

    override fun onDestroyView() {
        viewPager?.adapter = null
        tabLayout?.setupWithViewPager(null)
        viewPager = null
        tabLayout = null
        super.onDestroyView()

    }

    inner class ViewPagerUI : AnkoComponent<ViewPagerMediator>{
        override fun createView(ui: AnkoContext<ViewPagerMediator>): View = with(ui){
            verticalLayout {
                lparams(matchParent, matchParent)
                toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "ViewPagerMediator"
                    backgroundResource = R.color.colorPrimary
                }

                tabLayout = tabLayout {

                }.lparams(matchParent)

                viewPager = viewPager {

                }.lparams(matchParent, matchParent)
            }
        }
    }
}