package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.nfc.FormatException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.base.BaseFragment
import com.str.wardrobe.simpleMVVM.views.base.BaseScreen
import com.str.wardrobe.simpleMVVM.views.base.screenViewModel
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

class DressInfoEditableFragment : BaseFragment() {


    class Screen (
        val nameDressCategory: NamedCategory
            ): BaseScreen

    override val viewModel by screenViewModel<DressInfoEditableViewModel>()

    private lateinit var dressImage: ImageView
    private lateinit var dressName: EditText
    private lateinit var dressDescription: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dress_info_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dressImage = view.findViewById(R.id.imageOfDress)
        dressName = view.findViewById(R.id.nameOfCategory_edit)
        dressDescription = view.findViewById(R.id.descriptionOfCategory_edit)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        // Определение EditText названия одежды
        dressName.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setDressName(text.toString())
                } else {
                    dressName.error = null
                }

            } catch (_: FormatException) {

            }
        }

        // Определение EditText названия одежды
        dressDescription.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setDressDescription(text.toString())
                } else {
                    dressName.error = null
                }

            } catch (_: FormatException) {

            }
        }

    }




}