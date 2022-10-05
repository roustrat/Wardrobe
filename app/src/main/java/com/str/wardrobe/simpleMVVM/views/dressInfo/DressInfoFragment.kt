package com.str.wardrobe.simpleMVVM.views.dressInfo

import android.nfc.FormatException
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.base.BaseFragment
import com.str.wardrobe.simpleMVVM.views.base.BaseScreen
import com.str.wardrobe.simpleMVVM.views.base.screenViewModel
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

class DressInfoFragment : BaseFragment() {


    class Screen (
        val nameDress: NamedDress
            ): BaseScreen

    override val viewModel by screenViewModel<DressInfoViewModel>()

    private lateinit var dressName: TextView
    private lateinit var dressDescription: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.dress_info_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        dressName = view.findViewById(R.id.nameOfCategory)
        dressDescription = view.findViewById(R.id.descriptionOfCategory)

        dressName.text = viewModel.currentDress.name
        dressDescription.text = viewModel.currentDress.description

        super.onViewCreated(view, savedInstanceState)
    }


}