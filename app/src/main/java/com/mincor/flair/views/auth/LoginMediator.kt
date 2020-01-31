package com.mincor.flair.views.auth

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.mincor.flair.R
import com.mincor.flair.utils.Consts.FONT_SIZE_12
import com.mincor.flair.utils.Consts.FONT_SIZE_14
import com.mincor.flair.utils.Consts.ROUND_CORNERS_16
import com.mincor.flair.utils.dip8
import com.rasalexman.flaircore.animation.LinearAnimator
import com.rasalexman.flaircore.ext.log
import com.rasalexman.flaircore.interfaces.*
import com.rasalexman.flaircore.patterns.mediator.Mediator
import com.rasalexman.flairreflect.mediatorLazy
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick


/**
 * Created by Alex on 22.01.2017.
 */

class LoginMediator : Mediator() {

    override fun createLayout(context: Context): View = LoginUI().createView(AnkoContext.create(context, this))
    private val registerMediator: RegistrationMediator by mediatorLazy()
    private val forgotPassMediator: ForgotPassMediator by mediatorLazy()

    private var emailET:EditText? = null
    private var passwordET:EditText? = null

    internal fun singInUser() {
        log { "START LOGIN IN " }

        // Reset errors.
        emailET!!.error = null
        passwordET!!.error = null

        val emailStr = emailET!!.text.toString()
        val passStr = passwordET!!.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid emailET address.
        // There was an error; don't attempt login and focus the first
        // form field with an error.
        when {
            emailStr.isEmpty() -> {
                emailET!!.error = string(R.string.notSetEmailTF)
                focusView = emailET
                cancel = true
            }
            passStr.isEmpty() -> {       // Check for a valid password, if the user entered one.
                passwordET!!.error = string(R.string.notSetPassTF)
                focusView = passwordET
                cancel = true
            }
            passStr.length < 5 -> {  // Check for a valid password, if the user entered one.
                passwordET!!.error = string(R.string.minPassErrorTF) + " " + 5
                focusView = passwordET
                cancel = true
            }
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            popToBack(LinearAnimator())
        }
    }


    private class LoginUI : AnkoComponent<LoginMediator> {
        override fun createView(ui: AnkoContext<LoginMediator>): View = with(ui) {
            verticalLayout {
                background = gradientBg(arrayOf(ui.owner.color(R.color.startColor), ui.owner.color(R.color.endColor)))
                gravity = Gravity.CENTER

                ui.owner.emailET = editText {
                    background = ui.owner.roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = ui.owner.color(R.color.colorPrimaryText)
                    hint = ui.owner.string(R.string.emailTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }.lparams(ui.owner.wdthProc(0.6f))

                ui.owner.passwordET = editText {
                    background = ui.owner.roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = ui.owner.color(R.color.colorPrimaryText)
                    hint = ui.owner.string(R.string.passTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }.lparams(ui.owner.wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.logInTF) {
                    background = ui.owner.roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    onClick {
                        ui.owner.singInUser()
                    }
                }.lparams(ui.owner.wdthProc(0.6f), ui.owner.hdthProc(0.06f)) {
                    topMargin = dip8()
                }

                button(R.string.registerTF) {
                    backgroundColor= Color.TRANSPARENT
                    textSize = FONT_SIZE_12
                    textColor = Color.WHITE
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    onClick {
                        ui.owner.registerMediator.show(LinearAnimator())
                    }
                }.lparams(ui.owner.wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.forgotPassTF) {
                    backgroundColor= Color.TRANSPARENT
                    textSize = FONT_SIZE_12
                    textColor = Color.WHITE
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    onClick {
                        ui.owner.forgotPassMediator.show(LinearAnimator())
                    }
                }.lparams(ui.owner.wdthProc(0.6f))

                button(R.string.backTF) {
                    backgroundColor= Color.TRANSPARENT
                    textSize = FONT_SIZE_12
                    textColor = Color.WHITE
                    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    onClick {
                        ui.owner.popToBack(LinearAnimator())
                    }
                }.lparams(ui.owner.wdthProc(0.6f))
            }
        }
    }
}
