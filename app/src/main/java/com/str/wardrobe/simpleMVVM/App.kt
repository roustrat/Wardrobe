package com.str.wardrobe.simpleMVVM

import android.app.Application
import android.content.Context
import com.str.foundation.BaseApplication
import com.str.foundation.model.BaseRepository
import com.str.wardrobe.simpleMVVM.model.WardrobeRepository

/**
 * Here we store instances of model layer classes.
 */
class App : Application(), BaseApplication {

    /**
     * Place your repositories here, now we have only 1 repository
     */
//    override val repositories: List<BaseRepository> = listOf<BaseRepository>(WardrobeRepository.get())

    val repositories1: List<BaseRepository> by lazy { listOf<BaseRepository>(WardrobeRepository.get()) }

    // Попытка костыля
//    fun getRepository(context: Context) = listOf<WardrobeRepository>(WardrobeRepository(context).apply {
//        initialize(context)
//        get()
//    })


    override fun onCreate() {
        WardrobeRepository.initialize(this)
        super.onCreate()
    }

}