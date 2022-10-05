package com.str.wardrobe.simpleMVVM.views.dressInfo

import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.str.wardrobe.R
import com.str.foundation.views.BaseFragment
import com.str.foundation.views.BaseScreen
import com.str.foundation.views.screenViewModel
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

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