package com.gesecur.app.ui.common.dialog.material

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gesecur.app.R
import com.gesecur.app.domain.models.Product


/**
 * A simple [Fragment] subclass.
 */
class SearchProductDialog(context: Context,
                          val products: List<Product>,
                          val callback: ((Product) -> Unit)) : Dialog(context) {

    lateinit var recyclerView: RecyclerView
    lateinit var btnClose : ImageView
    lateinit var editFilter : EditText

    val adapter = SearchProductAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.fragment_material_selector)

        configureView()
        fillData()
    }

    private fun configureView() {
        recyclerView = findViewById(R.id.rv_filter)
        btnClose = findViewById(R.id.btn_close)
        editFilter = findViewById(R.id.edit_material_search)

        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        adapter.onItemClick = {

            callback.invoke(it)
            dismiss()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        btnClose.setOnClickListener {
            dismiss()
        }

        editFilter.addTextChangedListener {
            filter(it.toString())
        }

    }

    private fun fillData() {
        adapter.submitList(products)
    }

    public override fun onStart() {
        super.onStart()

        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window?.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimary))
//            window?.setNavigationBarColor(ContextCompat.getColor(context, android.R.color.black))
//        }
    }

    private fun filter(text: String) {
        adapter.submitList(
            if(text.isEmpty()) products
            else {
                products.filter {
                    it.description?.lowercase()?.contains(text.lowercase()) ?: false
                }
            })
    }
}
