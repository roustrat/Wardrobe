package com.str.wardrobe.simpleMVVM.views.dressInfo

import com.str.wardrobe.simpleMVVM.views.Navigator
import com.str.wardrobe.simpleMVVM.views.UiActions
import com.str.wardrobe.simpleMVVM.views.base.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

class DressInfoViewModel (
    private val repository: WardrobeRepository,
    screen: DressInfoFragment.Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions
        ) : BaseViewModel ()  {

        val currentDress: NamedDress = screen.nameDress

}