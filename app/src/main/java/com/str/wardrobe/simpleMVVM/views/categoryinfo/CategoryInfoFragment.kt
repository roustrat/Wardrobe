package com.str.wardrobe.simpleMVVM.views.categoryinfo

import android.nfc.FormatException
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel

class CategoryInfoFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CategoryInfoViewModel>()

    private lateinit var categoryNameInput: TextInputLayout
    private lateinit var categoryNameEdit: TextInputEditText

    private lateinit var categoryDescription: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.category_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        categoryNameInput = view.findViewById(R.id.nameOfCategory_input)
        categoryNameEdit = view.findViewById(R.id.nameOfCategory_edit)

        categoryDescription = view.findViewById(R.id.descriptionOfCategory_edit)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        // Определение EditText названия категории
        categoryNameEdit.doOnTextChanged { text, _, _, _ ->
            try {
                if (text != null) {
                    viewModel.setCategoryName(text.toString())
                } else {
                    categoryNameEdit.error = null
                }

            } catch (_: Exception) {

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_category_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_category -> {
                if (viewModel.currentCategory.name == "") {
                    categoryNameEdit.error
                } else {
                    if (viewModel.currentCategory.description == "") {
                        categoryDescription.error
                    } else {
                        viewModel.saveCategory()
                    }
                }
                true
            }
            R.id.cancel_category -> {
                viewModel.closeWithoutSaveCategory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}