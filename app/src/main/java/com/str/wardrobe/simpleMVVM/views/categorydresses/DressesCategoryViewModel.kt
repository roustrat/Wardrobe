package com.str.wardrobe.simpleMVVM.views.categorydresses

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.str.wardrobe.simpleMVVM.views.Navigator
import com.str.wardrobe.simpleMVVM.views.UiActions
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
    val allCategory : LiveData<List<NamedCategory?>> = repository.getAllCategory()
    val allDresses : LiveData<List<NamedDress?>> = repository.getAvailableDresses()

    // Current Category
    // Возможен конфликт между этими асинхронными задачачи, соответственно краш благодаря !!
    // Нормально реализовать current значения. В репозитории они уже есть. Возможно туда этот код кинуть, но без инстанса репы что делать там??
    // Нужно ли реализовать mergeSources из примера?
    var currentCategory : NamedCategory? = allCategory.value?.first()
    var currentDresses : LiveData<List<NamedDress>> = repository.getDressesOfCategory(currentCategory?.name)
    var currentDress : NamedDress? = currentDresses.value?.first()

    private val categoryListener : CategoryListener = {
        currentCategory = it
        currentDresses = repository.getDressesOfCategory(it.name)
    }

    private val dressListener : DressListener = {
        val screen = DressInfoFragment.Screen(it)
        navigator.launch(screen)
        currentDress = it
    }


    init {
        mergeSources()
        if (currentCategory != null) {
            repository.addListenerToCategory(categoryListener, currentCategory!!)
        }
        if (currentDress != null) {
            repository.addListenerToDress(dressListener, currentDress!!)
        }
    }

    /*
    По сути можно вывести из [_allCategory] и [_allDresses] с помощью методов set при приведении к нему
     */
//    private var categoryByName : LiveData<NamedCategory> = wardrobeRepository.getCategoryByName(currentCategory?.name)
//    private var dressByName : LiveData<NamedDress> = wardrobeRepository.getDressByName(currentCategory?.name)


    fun addDress(context: Context) {
        if (currentCategory == null) {
            Toast.makeText(context, "Пустая категория", Toast.LENGTH_SHORT).show()

        } else {
            val screen = DressInfoEditableFragment.Screen(currentCategory as NamedCategory)
            navigator.launch(screen)
        }

    }

    fun addCategory(category: NamedCategory) {
        repository.addCategory(category)
    }

    override fun onDressChosen(namedDress: NamedDress) {
        currentDress = namedDress
    }

    override fun onCategoryChosen(namedCategory: NamedCategory) {
        currentCategory = namedCategory
    }

    private fun mergeSources() {
        val allCategory = allCategory.value
        val currentCategory = allCategory?.first() ?: return
        val currentDresses = allDresses.value?.toSet()?.filter { it?.category == currentCategory.name } ?: return
    }



}