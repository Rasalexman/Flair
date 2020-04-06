package com.mincor.flair.views

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.ContactsContract
import android.view.View
import android.widget.ListView
import androidx.core.content.ContextCompat
import com.mincor.flair.R
import com.mincor.flair.adapters.SelectedListAdapter
import com.rasalexman.flaircore.animation.BackLinearAnimator
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.animation.NextLinearAnimator
import com.rasalexman.flaircore.ext.*
import com.rasalexman.flaircore.interfaces.IMediator
import com.rasalexman.flairreflect.showMediator
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*


class MVPMediator : ToolbarMediator() {

    override var hasOptionalMenu: Boolean = true
    override var hashBackButton: Boolean = true

    private var listView: ListView? = null

    override fun createLayout(context: Context): View = MvpUi().createView(AnkoContext.create(context, this))

    override fun onAddedView(view: View) {
        super.onAddedView(view)
        listView?.adapter = SelectedListAdapter(
                facade.view.mediatorBackStack.asSequence().mapTo(mutableListOf()) {
                    it.mediatorName
                },
                ::onItemSelectedHandler
        )
    }

    private fun onItemSelectedHandler(selectedTag: String) {
        popTo<IMediator>(selectedTag, LinearAnimator())
    }

    fun showFuncyMVPHandler() {
        activity.toast("FANCY HELLO FROM MVP").show()
    }

    private fun pickContact() {
        val pickContactIntent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        pickContactIntent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, 1001)

        // Here, thisActivity is the current activity
        /*if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
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
        }*/
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
                    activity.toast("PERMISSION GRANTED").show()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    activity.toast("PERMISSION DENIED").show()
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
        val nextMediatorTag = UUID.randomUUID().toString()
        //showMediator<MVPMediator>(NextLinearAnimator(), nextMediatorTag)
        this.showMediator(NextLinearAnimator(), nextMediatorTag) { MVPMediator() }
    }

    fun showMVVMAGAIN() {
        showMediator<MainMediator>(BackLinearAnimator())
    }

    private fun showUserListAgain() {
        showMediator<UserListsMediator>(BackLinearAnimator())
    }

    /*override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_list, menu)
    }*/

    class MvpUi : AnkoComponent<MVPMediator> {
        override fun createView(ui: AnkoContext<MVPMediator>) = with(ui) {
            verticalLayout {
                lparams(matchParent, matchParent)

                ui.owner.toolBar = toolbar {
                    setTitleTextColor(ContextCompat.getColor(ctx, android.R.color.white))
                    title = "MVP MEDIATOR"
                    backgroundResource = R.color.colorPrimary
                }

                textView("HELLO WoRlD from ${ui.owner.mediatorName}")

                button("back") {
                    onClick {
                        ui.owner.popToBack(BackLinearAnimator())
                    }
                }

                button("nextMVP mediator") {
                    onClick {
                        ui.owner.onShowAnotherMediator()
                    }
                }

                button("MVVM AGAIN") {
                    onClick {
                        ui.owner.showMVVMAGAIN()
                    }
                }

                button("USER LIST AGAIN") {
                    onClick {
                        ui.owner.showUserListAgain()
                    }
                }

                button("OPEN CONTACTS") {
                    onClick {
                        ui.owner.pickContact()
                    }
                }

                ui.owner.listView = listView {
                    divider = ColorDrawable(Color.GRAY)
                    dividerHeight = dip(1)
                }.lparams(matchParent, matchParent, 1f) {
                    margin = dip(16)
                }
            }
        }
    }
}