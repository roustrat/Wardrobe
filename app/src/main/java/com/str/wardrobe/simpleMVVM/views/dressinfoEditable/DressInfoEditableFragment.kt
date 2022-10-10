package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.nfc.FormatException
import android.os.Bundle
import android.view.*
import android.widget.*
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
    private lateinit var categoryChooseSpin: Spinner
    private lateinit var dressDescription: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewModel.allCategoriesName.observe(viewLifecycleOwner) {
            if (it != null) {
                categoryChooseSpin.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item,
                    viewModel.allCategoriesName.value ?: emptyList()
                )
            }

        }

        return inflater.inflate(R.layout.dress_info_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dressImage = view.findViewById(R.id.imageOfDress)
        dressName = view.findViewById(R.id.nameOfDress_edit)
        dressDescription = view.findViewById(R.id.descriptionOfDress_edit)

        // Определение Spinner выбора единицы измерения напряжения
        categoryChooseSpin = view.findViewById(R.id.category_choose_spin)
        activity?.let {
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModel.allCategoriesName.value ?: emptyList()
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                categoryChooseSpin.adapter = adapter
            }
        }
        // Create an ArrayAdapter using the string array and a default spinner layout
//        val adapter = ArrayAdapter(requireContext(),
//            android.R.layout.cat,
//            viewModel.allCategoriesName.value!!.toTypedArray()
//        )
//        categoryChooseSpin.adapter = adapter

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

        // Определение слушателя для Spinner выбора категории
        categoryChooseSpin.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                viewModel.currentDress.category =
                    categoryChooseSpin.adapter.getItem(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

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