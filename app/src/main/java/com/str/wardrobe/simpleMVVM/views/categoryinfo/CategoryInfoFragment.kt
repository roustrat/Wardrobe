package com.str.wardrobe.simpleMVVM.views.categoryinfo

import android.nfc.FormatException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.base.BaseFragment
import com.str.wardrobe.simpleMVVM.views.base.BaseScreen
import com.str.wardrobe.simpleMVVM.views.base.screenViewModel

class CategoryInfoFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CategoryInfoViewModel>()

    private lateinit var categoryName: EditText
    private lateinit var categoryDescription: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        categoryName = view.findViewById(R.id.nameOfCategory_edit)
        categoryDescription = view.findViewById(R.id.descriptionOfCategory_edit)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {

        // Определение EditText названия категории
        categoryName.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setCategoryName(text.toString())
                } else {
                    categoryName.error = null
                }

            } catch (_: FormatException) {

            }
        }

        // Определение EditText названия категории
        categoryDescription.doOnTextChanged { text, start, before, count ->
            try {
                if (text != null) {
                    viewModel.setCategoryDescription(text.toString())
                } else {
                    categoryDescription.error = null
                }

            } catch (_: FormatException) {

            }
        }

        super.onStart()
    }
}