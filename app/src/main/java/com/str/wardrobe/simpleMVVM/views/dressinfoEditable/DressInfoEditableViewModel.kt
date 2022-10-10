package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import androidx.lifecycle.LiveData
import com.str.wardrobe.R
import com.str.foundation.navigator.Navigator
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment.Screen

class DressInfoEditableViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val repository: WardrobeRepository
) : BaseViewModel()  {

    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()

    var allCategoriesName : LiveData<List<String>> = repositoryPublic.getCategoriesName()

    val currentDress: NamedDress = NamedDress("", screen.nameDressCategory.name, R.drawable.empty_photo, "")

    fun setDressName(name: String) {
        currentDress.name = name
    }

    fun setDressDescription(description: String) {
        currentDress.description = description
    }

    fun saveDress() {
        repositoryPublic.addDress(currentDress)
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

    fun closeWithoutSaveDress() {
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }



}