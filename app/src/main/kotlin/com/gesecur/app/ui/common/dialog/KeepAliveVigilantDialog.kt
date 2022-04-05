package com.gesecur.app.ui.common.dialog;

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.gesecur.app.R
import com.gesecur.app.databinding.FinishOperatorWorkDialogBinding

class KeepAliveVigilantDialog(
    context: Context,
    val callback: ((Int) -> Unit)
) : Dialog(context) {

    companion object {
        const private val MIN_MINUTES_FOR_KEEP_ALIVE = 15
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.keep_alive_vigilant_dialog)

        configureView()
    }


    private fun configureView() {
        findViewById<Button>(R.id.btn_ok).setOnClickListener {
            onAccept()
        }
    }

    private fun onAccept() {

        with(findViewById<EditText>(R.id.edit_time).text) {
            if(isNullOrEmpty()) return

            val extra = toString().toInt()

            if(extra < MIN_MINUTES_FOR_KEEP_ALIVE) return

            callback(extra)
            dismiss()
        }

    }

    public override fun onStart() {
        super.onStart()

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}