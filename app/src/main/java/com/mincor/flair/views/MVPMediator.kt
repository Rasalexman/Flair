package com.mincor.flair.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.widget.ListView
import com.mincor.flair.R
import com.mincor.flair.adapters.SelectedListAdapter
import com.mincor.flair.proxies.MVPProxy
import com.mincor.flair.proxies.vo.Tag
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.core.view.View
import com.mincor.flairframework.interfaces.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick

class MVPMediator : ToolbarMediator() {

    override var hasOptionalMenu: Boolean = true

    private val presenterProxy: MVPProxy by proxyLazy(this)

    private var listViw:ListView? = null

    override fun createLayout(context: Context): android.view.View = MvpUi().createView(AnkoContext.create(context, this))

    override fun onCreatedView(context:android.view.View) {
        super.onCreatedView(context)
        setHomeButtonEnable()
        presenterProxy.lazynessFunctionCall()
    }

    override fun onAddedView() {
        listViw?.adapter = SelectedListAdapter(
                View.getInstance(this.multitonKey).mediatorBackStack.mapTo(arrayListOf()) {it.mediatorName!!}.toMutableList(),
                ::onItemSelectedHandler
        )
    }

    private fun onItemSelectedHandler(selectedTag:String) {
        popTo<IMediator>(selectedTag, LinearAnimator())
    }

    fun showFuncyMVPHandler() {
        activity.toast("FANCY HELLO FROM MVP").show()
    }

    fun coroutinesResponceHander(resp:List<Tag>?) {
        activity.toast("HELLO coroutinesResponceHander $resp").show()
    }

    fun showErrorHandler(error:String) {
        activity.alert {
            title = "WARNING"
            message = error
            okButton {
                it.dismiss()
            }
        }
    }

    fun onShowAnotherMediator() {
        val mediator = facade.retrieveMediator<MVPMediator>("str${Math.random()*10000*10000}")
        mediator.show(LinearAnimator())
    }

    fun showMVVMAGAIN (){
        facade.retrieveMediator<MVVMMediator>().show()
    }

    private fun showUserListAgain() {
        facade.retrieveMediator<UserListsMediator>().show()
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }*/

    inner class MvpUi : AnkoComponent<MVPMediator> {
        override fun createView(ui: AnkoContext<MVPMediator>) = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "MVP MEDIATOR"
                    backgroundResource = R.color.colorPrimary
                }

                textView("HELLO WoRlD from $mediatorName")

                button("back") {
                    onClick {
                        popToBack(LinearAnimator())
                    }
                }

                button("nextMVP mediatorLazy") {
                    onClick {
                        onShowAnotherMediator()
                    }
                }

                button("MVVM AGAIN") {
                    onClick {
                        showMVVMAGAIN()
                    }
                }

                button("USER LIST AGAIN") {
                    onClick {
                        showUserListAgain()
                    }
                }

                listViw = listView {
                    divider = ColorDrawable(Color.GRAY)
                    dividerHeight = dip(1)
                }.lparams(matchParent, matchParent) {
                    margin = dip(16)
                }
            }
        }
    }
}