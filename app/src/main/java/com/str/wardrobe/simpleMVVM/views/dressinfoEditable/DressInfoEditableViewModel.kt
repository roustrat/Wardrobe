package com.str.wardrobe.simpleMVVM.views.dressinfoEditable

import android.net.Uri
import androidx.lifecycle.LiveData
import com.str.foundation.camerax.CameraXFragment
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
    var photoFile: File? = null
    var photoUri: Uri? = null

    var allCategoriesName : LiveData<List<String>> = repositoryPublic.getCategoriesName()

    private val dressId = screen.dressId
    val dressExists = screen.dressExists
    var currentDressFromRoom = repositoryPublic.getDressById(dressId)
    var dress : NamedDress? = null
    var currentDress : NamedDress? = null

    fun setDressName(name: String) {
        currentDress!!.name = name
    }

    fun setDressDescription(description: String) {
        currentDress!!.description = description
    }

    fun saveDress() {
        repositoryPublic.updateDress(currentDress!!)
        val screen = DressesCategoryFragment.Screen()
        navigator.launchWithRemove(screen)
    }

    fun closeWithoutSaveDress(dressExists : Boolean, save : Boolean) {
        if (dressExists) {
            if (save) {
                repositoryPublic.updateDress(currentDress!!)
            }
        } else {
            if (!save) {
                deleteDress()
            }
        }
        val screen = DressesCategoryFragment.Screen()
        navigator.launchWithRemove(screen)
    }

    private fun deleteDress() {
        repositoryPublic.deleteDress(currentDress!!)
    }

//    fun goBack() {
//        navigator.goBack()
//    }

    fun useCameraX() {
        val screen = CameraXFragment.Screen(currentDress as NamedDress)
        navigator.launch(screen)
    }

    private fun getPhotoFile(dress: NamedDress): File {
        return repositoryPublic.getPhotoFile(dress)
    }

//    fun updateDB() {
//        repositoryPublic.updateDress(currentDress!!)
//    }

    fun loadDress() {
//        if (currentDress == null) {
//            val dress = NamedDress()
//            repositoryPublic.addDress(dress)
//            currentDress = dress
//            uiActions.toast(currentDress!!.imgId.toString())
//            photoFile = getPhotoFile(dress)
//        } else {
//
//        }
        photoFile = getPhotoFile(currentDress!!)
    }

    fun errorToast(name: String) {
        uiActions.toast("$name is empty")
    }

    fun backFragmentScreen() : DressesCategoryFragment.Screen = DressesCategoryFragment.Screen()

}