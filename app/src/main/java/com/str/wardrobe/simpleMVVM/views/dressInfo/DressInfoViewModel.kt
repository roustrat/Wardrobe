package com.str.wardrobe.simpleMVVM.views.dressInfo

import com.str.wardrobe.simpleMVVM.views.base.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment.Screen

class DressInfoViewModel (
    screen: Screen

        ) : BaseViewModel ()  {

        val currentDress: NamedDress = screen.nameDress

}