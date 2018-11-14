package com.mincor.flair.views.auth

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import com.mincor.flair.R
import com.mincor.flair.utils.Consts.FONT_SIZE_12
import com.mincor.flair.utils.Consts.FONT_SIZE_14
import com.mincor.flair.utils.Consts.ROUND_CORNERS_16
import com.mincor.flair.utils.dip8
import com.rasalexman.flairframework.core.animation.LinearAnimator
import com.rasalexman.flairframework.interfaces.*
import com.rasalexman.flairframework.patterns.mediator.Mediator
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by Alex on 22.01.2017.
 */

class ForgotPassMediator : Mediator() {
    override fun createLayout(context: Context): View = ForgotUI().createView(AnkoContext.Companion.create(context, this))

    internal var emailET: EditText? = null

    internal fun onSubmitHandler() {
        val emailStr = emailET!!.text.toString()
        if (emailStr.isEmpty()) {
            //emailET!!.error string R.string.notSetEmailTF
            emailET!!.requestFocus()
        } else {

        }
    }

    private inner class ForgotUI : AnkoComponent<ForgotPassMediator> {
        override fun createView(ui: AnkoContext<ForgotPassMediator>): View = with(ui) {
            verticalLayout {
                background = gradientBg(arrayOf(color(R.color.startColor), color(R.color.endColor)))
                gravity = Gravity.CENTER

                emailET = editText {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14
                    textColor = color(R.color.colorPrimaryText)
                    hint = string(R.string.emailTF)
                    setPadding(dip8(), dip8(), dip8(), dip8())
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                }.lparams(wdthProc(0.6f)) {
                    topMargin = dip8()
                }

                button(R.string.submitTF) {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_14

                    onClick {
                        onSubmitHandler()
                    }
                }.lparams(wdthProc(0.6f), hdthProc(0.06f)) {
                    topMargin = dip8()
                }

                button(R.string.backTF) {
                    background = roundedBg(Color.WHITE, ROUND_CORNERS_16, true)
                    textSize = FONT_SIZE_12
                    textColor = color(R.color.colorPrimaryText)

                    onClick {
                        popToBack(LinearAnimator())
                    }
                }.lparams(wdthProc(0.4f), hdthProc(0.06f)) {
                    topMargin = dip8()
                }
            }
        }
    }

    /*@BindView(R.id.editText8)
    internal var email: EditText? = null


    protected fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.controller_forgot_password, container, false)
    }


    @OnClick(R.id.textView4)
    internal fun showSingInHandler() {
        this.getRouter().popCurrentController()
    }

    @OnClick(R.id.button4)
    internal fun onSubmitHandler() {
        val emailStr = email!!.text.toString()
        if (!Support.isValidEmail(emailStr) || TextUtils.isEmpty(emailStr)) {
            email!!.error = getActivity().getString(R.string.notSetEmailTF)
            email!!.requestFocus()
        } else {
            Support.showHideProgressDialog(true, getActivity(), getActivity().getString(R.string.sendingEmailTF))
            ParseUser.requestPasswordResetInBackground(emailStr, this)
        }
    }

    override fun done(e: ParseException?) {
        Support.showHideProgressDialog(true, null, null)
        if (e == null) {
            // An email was successfully sent with reset instructions.
            Toast.makeText(getApplicationContext(),
                    getActivity().getString(R.string.sendEmailSuccessTF), Toast.LENGTH_SHORT).show()
        } else {
            // Something went wrong. Look at the ParseException to see what's up.
            Toast.makeText(getApplicationContext(),
                    getActivity().getString(R.string.sendEmailFailTF), Toast.LENGTH_SHORT).show()
        }
    }*/

}
