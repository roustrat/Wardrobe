package com.str.wardrobe

import android.app.Application
import com.str.wardrobe.simpleMVVM.views.model.WardrobeRepository

/**
 * Here we store instances of model layer classes.
 */
class App : Application() {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    var models = listOf<Any>()

    override fun onCreate() {
        super.onCreate()
            WardrobeRepository.initialize(this)
    }




}