package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.content.Context
import androidx.lifecycle.LiveData
import com.str.foundation.navigator.Navigator
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.baserepositories.CategoryListener
import com.str.wardrobe.simpleMVVM.model.baserepositories.DressListener
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress

class DressesCategoryViewModel (
    private val navigator: Navigator
        ) : BaseViewModel(), CategoriesAdapter.Listener, DressesAdapter.Listener {


    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()
    // Надо подумать как сделать их private
    var allCategory : LiveData<List<NamedCategory>> = repositoryPublic.getCategories()
    var currentCategory : NamedCategory? = null
    var allDresses : LiveData<List<NamedDress>> = repositoryPublic.getAvailableDresses()
    var currentDresses : List<NamedDress> = emptyList()
    var currentDress : NamedDress? = null
//    repositoryPublic.addListenerToCategory(categoryListener, repositoryPublic.currentCategory!!)
//    repositoryPublic.addListenerToDress(dressListener, repositoryPublic.currentDress!!)


    // Current Category
    // Возможен конфликт между этими асинхронными задачачи, соответственно краш благодаря !!
    // Нормально реализовать current значения. В репозитории они уже есть. Возможно туда этот код кинуть, но без инстанса репы что делать там??
    // Нужно ли реализовать mergeSources из примера?

    private val categoryListener : CategoryListener = {
        currentCategory = it
        // Точно не то, нужно чтобы при смещении фокуса менял
        currentDresses = allDresses.value?.takeWhile { it.category == currentCategory?.name } ?: emptyList()
    }

    private val dressListener : DressListener = {
        val screen = DressInfoFragment.Screen(it)
        navigator.launch(screen)
        currentDress = it
    }


    init {

//        mergeSources()

    }

    fun addDress(context: Context) {
        val screen = DressInfoEditableFragment.Screen(currentCategory as NamedCategory)
        navigator.launch(screen)

    }

    fun addCategory(category: NamedCategory) {
        repositoryPublic.addCategory(category)
    }

    override fun onDressChosen(namedDress: NamedDress) {
        currentDress = namedDress
    }

    override fun onCategoryChosen(namedCategory: NamedCategory) {
        currentCategory = namedCategory
    }

    private fun mergeSources() {
        allCategory = repositoryPublic.getCategories()

    }



}