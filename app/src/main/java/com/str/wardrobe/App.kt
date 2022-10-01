package com.str.wardrobe

import android.app.Application
import android.content.Context
import com.str.wardrobe.simpleMVVM.views.model.WardrobeRepository

/**
 * Here we store instances of model layer classes.
 */
class App : Application() {

    /**
     * Place your repositories here, now we have only 1 repository
     */
//    var models:List<Any> = listOf<Any>()

    fun addModels(context: Context): List<Any> {
        return listOf<Any>(WardrobeRepository.initialize(this))
    }

    override fun onCreate() {
        super.onCreate()
        WardrobeRepository.initialize(this)
    }




}