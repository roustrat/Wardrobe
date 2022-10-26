package com.str.wardrobe.simpleMVVM.model.baserepositories

import androidx.lifecycle.LiveData
import com.str.foundation.model.BaseRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

typealias CategoryListener = (NamedCategory) -> Unit
typealias DressListener = (NamedDress) -> Unit
typealias CategoriesListener = (List<NamedCategory>) -> Unit


interface WardrobeBaseRepository : BaseRepository {

    var allCategory: LiveData<List<NamedCategory>>

    var allDresses: LiveData<List<NamedDress>>?

    /**
     * Listen for the current category changes.
     * The listener is triggered immediately with the current value when calling this method.
     */
    fun addListenerToCategory(listeners: CategoriesListener)

    /**
     * Stop listening for the current category changes
     */
    fun removeListenerFromCategory(listeners: CategoriesListener)

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