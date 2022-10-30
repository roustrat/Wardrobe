package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.str.foundation.camerax.CameraXFragment
import com.str.foundation.navigator.Navigator
import com.str.foundation.uiactions.UiActions
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedCategory
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

    var currentDress: NamedDress? = null

    fun setDressName(name: String) {
        currentDress!!.name = name
    }

    fun setDressDescription(description: String) {
        currentDress!!.description = description
    }

    fun saveDress() {
        repositoryPublic.updateDress(currentDress!!)
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

    fun closeWithoutSaveDress() {
        val screen = DressesCategoryFragment.Screen()
        navigator.launch(screen)
    }

    fun goToPhoto() {
        val screen = CameraXFragment.Screen(currentDress as NamedDress)
        navigator.launch(screen)
    }

    private fun getPhotoFile(dress: NamedDress): File {
        return repositoryPublic.getPhotoFile(dress)
    }

    fun updateDB() {
        repositoryPublic.updateDress(currentDress!!)
    }

    fun loadDress() {
        if (currentDress == null) {
            val dress = NamedDress()
            repositoryPublic.addDress(dress)
            currentDress = dress
            uiActions.toast(currentDress!!.imgId.toString())
            photoFile = getPhotoFile(dress)
        }

    }


}