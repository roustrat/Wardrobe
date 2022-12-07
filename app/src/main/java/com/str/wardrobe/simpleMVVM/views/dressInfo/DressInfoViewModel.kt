package com.str.wardrobe.simpleMVVM.views.dressInfo

import com.str.foundation.navigator.Navigator
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment.Screen

class DressInfoViewModel (
    screen: Screen,
    private val navigator: Navigator) : BaseViewModel()  {

    val currentDress: NamedDress = screen.nameDress

    fun closeFragment() {
        val screen = DressesCategoryFragment.Screen()
        navigator.launchWithRemove(screen)
    }

}