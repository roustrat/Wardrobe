package com.str.wardrobe.simpleMVVM.views.categoryinfo

import com.str.wardrobe.simpleMVVM.views.Navigator
import com.str.wardrobe.simpleMVVM.views.UiActions
import com.str.wardrobe.simpleMVVM.views.base.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

class CategoryInfoViewModel (
    private val repository: WardrobeRepository,
    private val navigator: Navigator,
    private val uiActions: UiActions
) : BaseViewModel() {

    private val currentCategory: NamedCategory = NamedCategory("", "")

    fun setCategoryName(name: String) {
        currentCategory.name = name
    }

    fun setCategoryDescription(description: String) {
        currentCategory.description = description
    }

    fun saveCategory() {
        repository.addCategory(currentCategory)
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

}