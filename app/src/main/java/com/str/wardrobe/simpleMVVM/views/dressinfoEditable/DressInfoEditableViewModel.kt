package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import com.str.wardrobe.R
import com.str.wardrobe.simpleMVVM.views.Navigator
import com.str.wardrobe.simpleMVVM.views.base.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment.Screen

class DressInfoEditableViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val repository: WardrobeRepository
) : BaseViewModel ()  {

        val currentDress: NamedDress = NamedDress("", screen.nameDressCategory.name, R.drawable.empty_photo, "")

        fun setDressName(name: String) {
            currentDress.name = name
        }

        fun setDressDescription(description: String) {
            currentDress.description = description
        }

        fun saveDress() {
            repository.addDress(currentDress)
            repository.currentDresses = repository.getDressesOfCategory(currentDress.name)
            val screen = DressesCategoryFragment.Screen()
            navigator.launch(screen)
        }

        fun closeWithoutSaveDress() {
            val screen = DressesCategoryFragment.Screen()
            navigator.launch(screen)
        }



}