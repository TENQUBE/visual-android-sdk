package com.tenqube.webui.component.bottomsheet

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tenqube.webui.R
import com.tenqube.webui.dto.OpenSelectBox
import com.tenqube.webui.dto.SelectBoxItem

const val ARG_ITEM = "item"

class ItemListDialogFragment : BottomSheetDialogFragment() {

    lateinit var callback: (SelectBoxItem) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         return inflater.inflate(R.layout.fragment_bottom_sheet_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<FrameLayout>(R.id.close).setOnClickListener {
            dismiss()
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val arg = arguments?.getSerializable(ARG_ITEM) as OpenSelectBox
        callback = arg.callback
        recyclerView.adapter = ItemAdapter(setSelectedColor(arg.request.selectedColor), arg.request.data) {
            dismiss()
            callback(it)
        }
    }

    private fun setSelectedColor(color: String): Int {
        return try {
            Color.parseColor(color)
        } catch (e: Exception) {
            Color.parseColor("#ff051d")
        }
    }

    companion object {
        fun newInstance(item: OpenSelectBox): ItemListDialogFragment =
            ItemListDialogFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ITEM, item)
                }
            }
    }
}
