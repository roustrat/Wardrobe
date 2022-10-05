package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.content.Context
import com.str.wardrobe.simpleMVVM.views.Navigator
import com.str.wardrobe.simpleMVVM.views.base.BaseViewModel
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment
import com.str.wardrobe.simpleMVVM.views.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.views.model.baserepositories.CategoryListener
import com.str.wardrobe.simpleMVVM.views.model.baserepositories.DressListener
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.views.model.entities.NamedDress

class DressesCategoryViewModel (
    private val navigator: Navigator,
    private val repository: WardrobeRepository
        ) : BaseViewModel(), CategoriesAdapter.Listener, DressesAdapter.Listener {

    // Надо подумать как сделать их private
    val repositoryPublic: WardrobeRepository = this.repository

    // Current Category
    // Возможен конфликт между этими асинхронными задачачи, соответственно краш благодаря !!
    // Нормально реализовать current значения. В репозитории они уже есть. Возможно туда этот код кинуть, но без инстанса репы что делать там??
    // Нужно ли реализовать mergeSources из примера?

    var currentDress : NamedDress = repositoryPublic.currentDresses?.value!!.first()

    private val categoryListener : CategoryListener = {
        repositoryPublic.currentCategory = it
        // Точно не то, нужно чтобы при смещении фокуса менял
        repositoryPublic.currentDresses = repositoryPublic.getDressesOfCategory(it.name)
    }

    private val dressListener : DressListener = {
        val screen = DressInfoFragment.Screen(it)
        navigator.launch(screen)
        currentDress = it
    }


    init {
        mergeSources()
        repositoryPublic.addListenerToCategory(categoryListener, repositoryPublic.currentCategory!!)
        repositoryPublic.addListenerToDress(dressListener, currentDress)
    }

    fun addDress(context: Context) {
        val screen = DressInfoEditableFragment.Screen(repository.currentCategory as NamedCategory)
        navigator.launch(screen)

    }

    fun addCategory(category: NamedCategory) {
        repositoryPublic.addCategory(category)
    }

    override fun onDressChosen(namedDress: NamedDress) {
        currentDress = namedDress
    }

    override fun onCategoryChosen(namedCategory: NamedCategory) {
        repositoryPublic.currentCategory = namedCategory
    }

    private fun mergeSources() {

        repositoryPublic.allCategory = repositoryPublic.getCategories()
        repositoryPublic.currentCategory =  repositoryPublic.allCategory!!.value!!.first()
        repositoryPublic.currentDresses = repositoryPublic.getDressesOfCategory(repositoryPublic.currentCategory!!.name)
    }



}