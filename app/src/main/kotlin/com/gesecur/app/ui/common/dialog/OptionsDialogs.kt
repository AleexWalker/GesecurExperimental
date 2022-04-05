package com.gesecur.app.ui.common.dialog


import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.gesecur.app.R

class OptionsDialog<DATA>(context: Context, val options: List<DATA>, var listener: DialogSelectHandler<DATA>, val renderer: OptionsDialogRenderer<DATA>? = null) : AlertDialog.Builder(context, R.style.dialogTheme) {

    private var dialog: AlertDialog? = null

    var selectedOption: DATA? = null
    var selectedPosition = -1

    var suffix: String = ""

    init {
        init()
    }

    constructor(context: Context, options: List<DATA>, selectedPos: Int, listener: DialogSelectHandler<DATA>, renderer: OptionsDialogRenderer<DATA>?) : this(context, options, listener, renderer) {
        this.selectedPosition = selectedPos
        init()
    }

    constructor(context: Context, options: List<DATA>, selectedData: DATA?, listener: DialogSelectHandler<DATA>, renderer: OptionsDialogRenderer<DATA>?) : this(context, options, listener, renderer) {

        selectedOption = selectedData
        init()
    }

    private fun init() {
        setTitle(context.getString(R.string.UTILS_OPTIONS_DIALOG_TITLE))
        setCancelable(false)


        fillData()
    }

    override fun show(): AlertDialog {
        if (dialog == null)
            dialog = create()

        dialog!!.let {

            it.show()

            val topBottomSpace = context.resources.getDimensionPixelSize(R.dimen.gap_x2) * 2
            val params = it.window!!.attributes
            params.y = -topBottomSpace

            it.window!!.attributes = params

            return it
        }
    }

    private fun fillData() {
        var selectedIndex = -1
        val data = arrayOfNulls<CharSequence>(options.size)

        options.forEachIndexed { index, datum ->

            data[index] = renderer?.onRender(datum) ?: datum.toString().plus(suffix)

            if (datum == selectedOption || index == selectedPosition) {
                selectedIndex = index
            }
        }

        setSingleChoiceItems(data, selectedIndex) { dialog, index ->
            selectedPosition = index
            selectedOption = options[index]
        }

        setPositiveButton(R.string.UTILS_ACCEPT) { dialogInterface, which ->

            //Log.d(Constants.TAG, "selected option: $selectedOption")

            if (selectedOption != null)
                listener.onSelect(selectedOption!!, selectedPosition)
        }

        setNegativeButton(R.string.UTILS_CANCEL) { dialogInterface, i ->
            listener.onSelect(null, -1)
        }
    }


    interface DialogSelectHandler<DATA> {
        fun onSelect(data: DATA?, position: Int)
    }

    interface OptionsDialogRenderer<DATA> {
        fun onRender(data: DATA): String?
    }
}