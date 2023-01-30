package com.str.wardrobe.simpleMVVM.views.categoryinfo

import android.nfc.FormatException
import android.os.Bundle
import android.view.*
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.widget.doOnTextChanged
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.simpleMVVM.MainActivity

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

            // Тут как-то тоже нужно реализовать AlertDialog
            (activity as MainActivity?)!!.resetActionBar(true, DrawerLayout.LOCK_MODE_LOCKED_CLOSED, viewModel.backFragmentScreen())

            // The usage of an interface lets you inject your own implementation
            val menuHost: MenuHost = requireActivity()
            // Add menu items without using the Fragment Menu APIs
            // Note how we can tie the MenuProvider to the viewLifecycleOwner
            // and an optional Lifecycle.State (here, RESUMED) to indicate when
            // the menu should be visible
            menuHost.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_dress_fragment, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    // Handle the menu selection
                    return when (menuItem.itemId) {
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
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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

}