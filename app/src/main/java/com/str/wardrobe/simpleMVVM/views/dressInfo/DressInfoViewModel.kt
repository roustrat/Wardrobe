package com.str.wardrobe.simpleMVVM.views.dressInfo

import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment.Screen

class DressInfoViewModel (
    screen: Screen

        ) : BaseViewModel()  {

        val currentDress: NamedDress = screen.nameDress

}