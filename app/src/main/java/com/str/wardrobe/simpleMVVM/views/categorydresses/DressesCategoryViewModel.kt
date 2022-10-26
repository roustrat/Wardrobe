package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.str.foundation.navigator.Navigator
import com.str.foundation.uiactions.UiActions
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.baserepositories.CategoriesListener
import com.str.wardrobe.simpleMVVM.model.baserepositories.CategoryListener
import com.str.wardrobe.simpleMVVM.model.baserepositories.DressListener
import com.str.wardrobe.simpleMVVM.model.baserepositories.WardrobeBaseRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.dressInfo.DressInfoFragment
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment

class DressesCategoryViewModel (
    private val navigator: Navigator,
    private val uiActions: UiActions
) : BaseViewModel(), CategoriesAdapter.Listener, DressesAdapter.Listener {

    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()

    // Надо подумать как сделать их private
    var allCategory : LiveData<List<NamedCategory>> = repositoryPublic.getCategories()
    var currentCategory : MutableLiveData<NamedCategory> = MutableLiveData<NamedCategory>()
    var allDresses : LiveData<List<NamedDress>> = repositoryPublic.getAvailableDresses()
    var currentDresses : LiveData<List<NamedDress>> = Transformations.switchMap(currentCategory) {
        repositoryPublic.getDressesOfCategory(it.name)
    }
    //    var currentDresses : LiveData<List<NamedDress>> = repositoryPublic.getDressesOfCategory(
//        currentCategory.value?.name ?: "Футболки"
//    )
    var currentDress : NamedDress? = null
//    repositoryPublic.addListenerToCategory(categoryListener, repositoryPublic.currentCategory!!)
//    repositoryPublic.addListenerToDress(dressListener, repositoryPublic.currentDress!!)



    // Current Category
    // Возможен конфликт между этими асинхронными задачачи, соответственно краш благодаря !!
    // Нормально реализовать current значения. В репозитории они уже есть. Возможно туда этот код кинуть, но без инстанса репы что делать там??
    // Нужно ли реализовать mergeSources из примера?

    private val categoryListeners : CategoriesListener = {

    }


//    private val dressListener : DressListener = {
//        val screen = DressInfoFragment.Screen(it)
//        navigator.launch(screen)
//        currentDress = it
//    }

    fun updateCategoriesValue() {
        repositoryPublic.allCategory = repositoryPublic.getCategories()
    }

    fun updateCurrentDressesValue() : LiveData<List<NamedDress>> {
        return repositoryPublic.getDressesOfCategory(currentCategory.value!!.name)
    }

    fun addListenersToCategory() {
        repositoryPublic.addListenerToCategory(categoryListeners)
    }

    fun addDress(context: Context) {
        val screen = DressInfoEditableFragment.Screen(currentCategory.value as NamedCategory)
        navigator.launch(screen)

    }

    fun addCategory(category: NamedCategory) {
        repositoryPublic.addCategory(category)
    }

    override fun onDressChosen(namedDress: NamedDress) {
        currentDress = namedDress
    }

    override fun onCategoryChosen(namedCategory: NamedCategory) {
        currentCategory.value = namedCategory
        uiActions.toast(namedCategory.name)
    }

    private fun mergeSources() {
        allCategory = repositoryPublic.getCategories() as MutableLiveData<List<NamedCategory>>

    }

    override fun onCategoryFocused(namedCategory: NamedCategory) {
        currentCategory.value = namedCategory
        currentDresses = repositoryPublic.getDressesOfCategory(namedCategory.name)
        uiActions.toast(namedCategory.name)
    }


}