package com.mincor.flair.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ListView
import com.mincor.flair.R
import com.mincor.flair.adapters.SelectedListAdapter
import com.mincor.flair.proxies.MVPProxy
import com.mincor.flair.proxies.vo.Tag
import com.mincor.flairframework.core.animation.LinearAnimator
import com.mincor.flairframework.ext.log
import com.mincor.flairframework.interfaces.*
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk25.coroutines.onClick


class MVPMediator : ToolbarMediator() {

    override var hasOptionalMenu: Boolean = true

    private val presenterProxy: MVPProxy by proxyLazy(this)

    private var listViw: ListView? = null

    override fun createLayout(context: Context): android.view.View = MvpUi().createView(AnkoContext.create(context, this))

    override fun onCreatedView(view: View) {
        super.onCreatedView(view)
        presenterProxy.lazynessFunctionCall()
    }

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        setHomeButtonEnable()
        listViw?.adapter = SelectedListAdapter(
                facade.view.mediatorBackStack.mapTo(arrayListOf()) { it.mediatorName!! }.toMutableList(),
                ::onItemSelectedHandler
        )
    }

    private fun onItemSelectedHandler(selectedTag: String) {
        popTo<IMediator>(selectedTag, LinearAnimator())
    }

    fun showFuncyMVPHandler() {
        activity.toast("FANCY HELLO FROM MVP").show()
    }

    fun coroutinesResponceHander(resp: List<Tag>?) {
        activity.toast("HELLO coroutinesResponceHander $resp").show()
    }

    private fun pickContact() {
        //val pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        //pickContactIntent.type = Phone.CONTENT_TYPE // Show user only contacts w/ phone numbers
        //startActivityForResult(pickContactIntent, 1001)

        // Here, thisActivity is the current activity
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                        1002)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        log { "THIS IS A RESPOND FROM ACTIVITY" }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        log { "THIS IS A REQUESTED PERMISSION RESULT" }

        when (requestCode) {
            1002 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    fun showErrorHandler(error: String) {
        activity.alert {
            title = "WARNING"
            message = error
            okButton {
                it.dismiss()
            }
        }.show()
    }

    fun onShowAnotherMediator() {
        showMediator<MVPMediator>("str${Math.random() * 10000 * 10000}", LinearAnimator())
    }

    fun showMVVMAGAIN() {
        showMediator<MVVMMediator>()
    }

    private fun showUserListAgain() {
        showMediator<UserListsMediator>()
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

                button("nextMVP mediator") {
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

                button("OPEN CONTACTS") {
                    onClick {
                        pickContact()
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