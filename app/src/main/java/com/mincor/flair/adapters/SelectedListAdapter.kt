package com.mincor.flair.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

/**
 * Created by a.minkin on 10.10.2017.
 */
typealias ListSelectCallback = (String) -> Unit

class SelectedListAdapter(
        private val tags: MutableList<String>,
        private val callback: ListSelectCallback? = null
) : BaseAdapter() {

    override fun getView(i: Int, convertView: View?, parent: ViewGroup?): View {
        val viewholder: TagsViewHolder
        val selectedTag: String = getItem(i)
        val view: View =
                if (convertView == null) {
                    viewholder = TagsViewHolder(parent!!.context)
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
                        viewholder.view.layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, viewholder.itemHdth)
                    }
                    viewholder.view.tag = viewholder
                    viewholder.view
                } else {
                    viewholder = convertView.tag as TagsViewHolder
                    convertView
                }

        viewholder.tagButton?.onClick {
            callback?.let { it(selectedTag) }
        }
        viewholder.selectedTag = selectedTag
        return view
    }

    override fun getItem(position: Int): String = tags[position]
    override fun getCount(): Int = tags.size
    override fun getItemId(position: Int): Long = position.toLong()

    class TagsViewHolder(context: Context) {
        var tagText: TextView? = null
        var tagButton: Button? = null
        val view: LinearLayout
        var itemHdth: Int = 0

        // выбранный ранее тэг
        var selectedTag: String? = null
            set(value) {
                field = value
                tagText?.text = value
            }

        init {
            view = with(context) {
                itemHdth = dip(56)
                linearLayout {
                    lparams(matchParent, itemHdth)
                    gravity = android.view.Gravity.CENTER_VERTICAL
                    tagText = textView {
                        textColor = Color.BLACK
                        textSize = 12f
                        gravity = android.view.Gravity.CENTER_VERTICAL
                    }.lparams(matchParent, matchParent) {
                        weight = 1F
                    }

                    tagButton = button("SELECT") {
                        textSize = 12f
                        backgroundColor = Color.TRANSPARENT
                    }.lparams {
                        rightMargin = dip(8)
                    }
                }
            }
        }
    }
}