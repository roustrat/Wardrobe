package com.str.wardrobe.simpleMVVM.views.dressInfo

import com.str.foundation.navigator.Navigator
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment.Screen
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment

class DressInfoViewModel (
    screen: Screen,
    private val navigator: Navigator) : BaseViewModel()  {

    val currentDress: NamedDress = screen.nameDress
    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()

    fun backFragmentScreen() : DressesCategoryFragment.Screen = DressesCategoryFragment.Screen()

    fun closeFragment() {
        val screen = DressesCategoryFragment.Screen()
        navigator.launchWithRemove(screen)
    }

    fun closeWithoutSaveDress() {
        deleteDress()
        val screen = DressesCategoryFragment.Screen()
        navigator.launchWithRemove(screen)
    }

    private fun deleteDress() {
        repositoryPublic.deleteDress(currentDress)
    }

    fun editDress() {
        val screen = DressInfoEditableFragment.Screen(repositoryPublic.getCategoryByName(currentDress.category).value!!)
        navigator.launchWithRemove(screen)
    }

}