package com.str.wardrobe.simpleMVVM.model.baserepositories

import androidx.lifecycle.LiveData
import com.str.foundation.model.BaseRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

typealias CategoryListener = (NamedCategory) -> Unit

interface CategoryBaseRepository: BaseRepository {

    var allCategory: LiveData<List<NamedCategory>>?
//
    var currentCategory: NamedCategory?

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

    var currentDresses: LiveData<List<NamedDress>>?

    /**
     * Listen for the current dress changes.
     * The listener is triggered immediately with the current value when calling this method.
     */
    fun addListenerToDress(listener: DressListener, dress: NamedDress)

    /**
     * Stop listening for the current dress changes
     */
    fun removeListenerFromDress(listener: DressListener)

}