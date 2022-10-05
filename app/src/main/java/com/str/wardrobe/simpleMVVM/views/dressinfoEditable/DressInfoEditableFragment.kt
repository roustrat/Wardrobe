package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.nfc.FormatException
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory

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

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_dress_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.add_dress -> {
                if (viewModel.currentDress.name == "") {
                    dressName.error
                } else {
                    if (viewModel.currentDress.description == "") {
                        dressDescription.error
                    } else {
                        viewModel.saveDress()
                    }
                }
                true
            }
            R.id.cancel_dress -> {
                viewModel.closeWithoutSaveDress()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}