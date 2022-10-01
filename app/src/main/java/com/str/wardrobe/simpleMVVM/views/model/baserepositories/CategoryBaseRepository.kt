package com.str.wardrobe.simpleMVVM.views.model.baserepositories

import com.str.wardrobe.simpleMVVM.views.model.BaseRepository
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory

typealias CategoryListener = (NamedCategory) -> Unit

interface CategoryBaseRepository: BaseRepository {

//    var allCategory: List<NamedCategory>
//
//    var currentCategory: NamedCategory

//    /**
//     * Get the category content by its name
//     */
//    fun getCategoryByName(name: String): NamedCategory

    /**
     * Listen for the current category changes.
     * The listener is triggered immediately with the current value when calling this method.
     */
    fun addListenerToCategory(listener: CategoryListener, category: NamedCategory)

    /**
     * Stop listening for the current category changes
     */
    fun removeListenerFromCategory(listener: CategoryListener)

}