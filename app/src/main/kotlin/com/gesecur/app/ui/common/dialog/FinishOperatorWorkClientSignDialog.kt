package com.gesecur.app.ui.common.dialog;

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.gesecur.app.R
import com.gesecur.app.databinding.FinishOperatorWorkDialogBinding

class FinishOperatorWorkClientSignDialog(
    context: Context,
    val callback: ((String, String, Boolean) -> Unit)
) : Dialog(context) {

    lateinit var cbFaults: CheckBox
    lateinit var dniEdit: EditText
    lateinit var obsEdit: EditText
    lateinit var okButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.finish_operator_client_sign)

        configureView()
    }


    private fun configureView() {
        okButton = findViewById<Button>(R.id.btn_ok)
        okButton.setOnClickListener {
            onAccept()
        }

        okButton.isEnabled = false

        dniEdit = findViewById<EditText>(R.id.edit_dni)
        obsEdit = findViewById<EditText>(R.id.edit_observation)
        dniEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                okButton.isEnabled = (dniEdit.text.isNotEmpty() && dniEdit.text.length > 8 && obsEdit.text.isNotEmpty())
            }
        })

        obsEdit.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                okButton.isEnabled = (dniEdit.text.isNotEmpty() && dniEdit.text.length > 8 && obsEdit.text.isNotEmpty())
            }
        })

        cbFaults = findViewById(R.id.cb_finished_faults)
    }

    private fun onAccept() {
        val dni = dniEdit.text.toString()
        val obs = findViewById<EditText>(R.id.edit_observation).text.toString()

        callback(dni, obs, cbFaults.isChecked)

        dismiss()
    }

    public override fun onStart() {
        super.onStart()

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}