package com.str.wardrobe.simpleMVVM.model.baserepositories

import androidx.lifecycle.LiveData
import com.str.foundation.model.BaseRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

typealias DressListener = (NamedDress) -> Unit

interface DressBaseRepository : BaseRepository {

    // Данный Dress
//    var currentDress: NamedDress?

    // Dresses данного Category
//    var currentDresses: LiveData<List<NamedDress>>?

    // All Dresses
    var allDresses: List<NamedDress>?

//    /**
//     * Get the list of all available dresses that may be chosen by the user.
//     */
//    fun getAvailableDresses(): List<NamedDress>
//
//    /**
//     * Get the dress content by its name
//     */
//    fun getDressByName(name: String): NamedDress

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