package com.str.foundation.camerax

import com.str.foundation.navigator.Navigator
import com.str.foundation.uiactions.UiActions
import com.str.foundation.views.BaseViewModel
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository
import com.str.wardrobe.simpleMVVM.model.entities.NamedDress
import java.io.File

class CameraXViewModel(
    screen: CameraXFragment.Screen,
    private val navigator: Navigator,
    private val repository: WardrobeRepository,
    private val uiActions: UiActions
) : BaseViewModel() {

    private val repositoryPublic: WardrobeRepository = WardrobeRepository.get()

    val currentDress = screen.namedDress

    fun getPhotoFile(dress: NamedDress): File {
        return repositoryPublic.getPhotoFile(dress)
    }

}