package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.str.foundation.navigator.Navigator
import com.str.foundation.uiactions.UiActions
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment
import com.str.wardrobe.simpleMVVM.views.dressinfoEditable.DressInfoEditableFragment.Screen
import java.io.File

class DressInfoEditableViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val repository: WardrobeRepository,
    private val uiActions: UiActions
) : BaseViewModel()  {

    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()
    lateinit var photoFile: File
    lateinit var photoUri: Uri

    var allCategoriesName : LiveData<List<String>> = repositoryPublic.getCategoriesName()
    private val dressIdLiveData = MutableLiveData<Int>()

    val currentDress: LiveData<NamedDress> =
        Transformations.switchMap(dressIdLiveData) {
            repositoryPublic.getDressById(it)
        }

    fun setDressName(name: String) {
        currentDress.value!!.name = name
    }

    fun setDressDescription(description: String) {
        currentDress.value!!.description = description
    }

    fun saveDress() {
        repositoryPublic.addDress(currentDress.value!!)
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

    fun closeWithoutSaveDress() {
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

    fun getPhotoFile(dress: NamedDress): File {
        return repositoryPublic.getPhotoFile(dress)
    }

    fun loadDress(id: Int) {
        if (dressIdLiveData.value == null) {
            repositoryPublic.addDress(NamedDress("", "", id, ""))
            dressIdLiveData.value = id
            uiActions.toast(id.toString())
        }

    }


}