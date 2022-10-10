package com.str.wardrobe.simpleMVVM.views.categoryinfo

import com.str.foundation.navigator.Navigator
import com.str.foundation.uiactions.UiActions
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory

class CategoryInfoViewModel (
    private val navigator: Navigator,
    private val uiActions: UiActions,
    private val repository: WardrobeRepository
) : BaseViewModel() {

    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()

    val currentCategory: NamedCategory = NamedCategory("", "")

    fun setCategoryName(name: String) {
        currentCategory.name = name
    }

    fun setCategoryDescription(description: String) {
        currentCategory.description = description
    }

    fun saveCategory() {
        repositoryPublic.addCategory(currentCategory)
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

    fun closeWithoutSaveCategory() {
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

}